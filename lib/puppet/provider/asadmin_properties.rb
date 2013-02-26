module Puppet::Provider::AsadminProperties
  # Sup. This module provides some supporting code for talking to Glassfish
  # entities that are backed by constellations of settings. In order to do
  # this, it keeps a "pristine" copy of the resource's properties (in
  # property_was).
  #
  # This module is meant to be used as a mixin within Puppet providers, and
  # makes assumptions about what instance variables are available. In
  # particular, classes including this module must provide a '@property_hash'
  # instance variable. Additionally, this mixin assumes the existence of both
  # class and instance methods named glassfish_cmd, with identical signatures.
  #
  # Interesting client methods (mostly private):
  #
  # * was(foo) and should(foo) return the "pristine" and "desired" state of a
  #   single field. You can directly manipulate the former using property_was,
  #   and the latter using @property_hash directly.
  #
  # * self.prefetch, which is a Puppet lifecycle method, is defined here. This
  #   calls self.create_default(name), which should return a new provider. By
  #   default this sets all self.glassfish_properties fields to :absent,
  #   :ensure to :absent, and :name to the passed name, but you can customize
  #   this if you feel fancy. This uses self.glassfish_search_base (which can
  #   return either a single string or an Array of strings) to determine where
  #   within Glassfish to look for existing objects.
  #
  # * self.prefetch also calls self.match_descriptor to parse individual lines
  #   of output. This should return a Regexp match-alike with three captures;
  #   the first capture will be taken as a resource name, the second as the
  #   Glassfish suffix of a resource property, and the third as its current
  #   value. match_descriptor receives the current search base as an argument,
  #   to help parse resources that can appear in multiple places in Glassfish.
  #
  # * flush, which is a Puppet lifecycle method, is defined here. It calls
  #   back to create! and destroy! as needed to create or destroy the
  #   Glassfish resource associated with the provider; these must also update
  #   the "pristine" state appropriately if they set any values via asadmin
  #   command-line options (but should not bother updating :ensure).
  #
  # * self.dotted_name(*parts) and dotted_name(*parts) ease construction of
  #   dotted name and dotted name patterns. Call 'em. They're lonely.
  #
  # * self.glassfish_properties should return a mapping of Glassfish property
  #   suffixes (as Strings) to Puppet property names (as symbols). You will
  #   want to override this.
  #
  # * glassfish_basename should return the "base" part of the managed
  #   resource's name in Glassfish. This calls back into
  #   self.glassfish_search_base by default, joining that to `name` with a
  #   dot. This is appropriate for most cases; glassfish_basename can be
  #   overridden if this is inappropriate. (If glassfish_search_base returns
  #   an Array, you must override glassfish_basename too.)
  #
  # * flush_dirty_properties emits Glassfish `set` commands to flush dirty
  #   settable fields back to Glassfish. This method calls to_glassfish(key) to
  #   determine the appropriate Glassfish name of the field; you may want to
  #   override this if the default of joining glassfish_basename and the key
  #   with a dot is insufficient.
  #
  # You also get the AsadminUtils module at both instance and class scope in
  # the dealio.

  include Puppet::Provider::AsadminUtils

  module ClassMethods
    include Puppet::Provider::AsadminUtils
    # Returns a mapping of Glassfish names (strings) to Puppet names (symbols)
    # with any punctuation conversion and so on.
    def glassfish_properties
      {}
    end

    # Creates an instance of this provider with the default configuration. All
    # Glassfish property fields 
    def create_default(fields)
      glassfish_properties.each do |glassfish_name, puppet_name|
        fields[puppet_name] ||= :absent
      end
      fields[:ensure] ||= :absent
      new(fields)
    end

    # Parse a descriptor line from `asadmin list` as a property value. This
    # implementation assumes that the output is structured as
    #
    #   SEARCHBASE.RESOURCE_NAME.SUBPROPERTY=VALUE
    #
    # with support for multiple layers of subproperties. However, this is not
    # always appropriate; override this method as needed.
    def match_descriptor(base, descriptor)
      pattern = /#{Regexp.quote(base + '.')}([^.=]*)[.]([^=]*)=(.*)/
      pattern.match(descriptor)
    end

    # Creates providers for all known instances in a Glassfish server. The
    # passed opts hash needs to contain any options necessary to connect to
    # Glassfish; see asadmin_cmd for details. This is normally called from
    # prefetch(resources), which passes a random resource's config as opts.
    def list(opts)
      # Parse asadmin output...
      elements = {}
      glassfish_search_base.to_a.each do |base|
        (asadmin_cmd 'get', dotted_name(base, '*'), opts).lines.each do |descriptor|
          descriptor.chomp!
          if match = self.match_descriptor(base, descriptor)
            name, property, value = match.captures
            elements[name] ||= {}
            elements[name][property] = value
          else
            Puppet.warning "Failed to parse descriptor: #{descriptor} (in #{self})"
          end
        end
      end

      # ... then convert to provider instances
      elements.map do |name, properties|
        fields = { :name => name, :ensure => :present }
        glassfish_properties.each do |asadmin_key, puppet_key|
          fields[puppet_key] = properties[asadmin_key] if not properties[asadmin_key].nil?
        end
        new(fields)
      end
    end

    # Prefetch instances of this provider matching resources in the passed
    # hash.
    def prefetch(resources)
      # XXX assume the first resource has credentials appropriate for
      # prefetching everything. We will never be called with zero resources.
      name, params = resources.first
      providers = by_key(list(params.to_hash)) { |provider| provider.name }

      resources.each do |name, resource|
        if (provider = providers[name]).nil?
          resource.provider = providers[name] = create_default(:name => name)
        else
          resource.provider = provider
        end
      end
    end
    
    # Assembles the passed list of parts into a Glassfish dotted name.
    def dotted_name(*parts)
      parts.join('.')
    end
  end

  def self.included(into)
    into.extend(ClassMethods)
  end

  attr_accessor :property_was

  # Set up new property-backed asadmin resources by cloning the incoming hash.
  # Chains to the next c'tor, arguments intact, and even makes a reasonable
  # effort to return the same thing the superclass c'tor does. You're welcome.
  def initialize(hash_or_resource, *args)
    self.property_was = hash_or_resource.dup
    super(hash_or_resource, *args)
  end

  # Returns true if the represented resource's desired state is that it exists.
  # This can be totally divorced from reality.
  def exists?
    should(:ensure) == :present
  end

  # Asserts that the represented resource's desired state is that it should
  # exist.
  def create
    @property_hash[:ensure] = :present
  end

  # Asserts that the represented resource's desired state is that it should
  # NOT exist.
  def destroy
    @property_hash[:ensure] = :absent
  end

  # Flush this resource back to Glassfish.
  def flush
    if should(:ensure) == :absent and was(:ensure) == :present
      destroy!
      property_was[:ensure] = :absent
    elsif should(:ensure) == :present and was(:ensure) == :absent
      smudge_glassfish_properties
      create!
      property_was[:ensure] = :present
    end
    
    if should(:ensure) == :present
      flush_dirty_properties
    end
  end

  private
    # Marks glassfish-property-derived fields as "dirty" by updating their
    # "desired" values from the underlying resource.
    def smudge_glassfish_properties
      self.class.glassfish_properties.each do |glassfish_name, puppet_name|
        @property_hash[puppet_name] = resource[puppet_name]
      end
    end

    # Returns the "pristine" value of a field, from property_was.
    def was(key)
      property_was[key]
    end

    # Returns the "desired" value of a field, from property_hash.
    def should(key)
      @property_hash[key]
    end

    # Asserts that the underlying resource is in a given state, overwriting
    # any `was(key)` value derived from Glassfish's state. Only call this if
    # you want the key to be considered clean, too, or you will be very sad
    # later.
    def is(key, new_value)
      property_was[key] = new_value
    end

    # Returns the value of a key if the key is "dirty" - that is, if it has been
    # changed from its "pristine" value. Otherwise, returns nil. (Puppet does
    # not make much use of 'false' or 'nil', so this is reasonably unambiguous.)
    def dirty(key)
      should(key) if should(key) != was(key)
    end

    # Returns the Glassfish dotted-name of this resource. Override me.
    def glassfish_basename
      dotted_name self.class.glassfish_search_base, name
    end

    # Make glassfish property names available in instance scope too. We're lazy
    # typists.
    def glassfish_properties
      self.class.glassfish_properties
    end

    # Assembles the passed list of parts into a Glassfish dotted name. This is
    # a wee dumb proxy to the class method of the same name -- override that
    # one if you're going to fuck with this.
    def dotted_name(*parts)
      self.class.dotted_name(*parts)
    end

    # Returns a hash of the fields that are (a) mappable to Glassfish
    # properties (b) set and (c) dirty.
    def dirty_properties
      dirty = {}
      glassfish_properties.each do |glassfish_suffix, puppet_name|
        if value = dirty(puppet_name)
          dirty[glassfish_suffix] = value
        end
      end
      dirty
    end
    
    # Writes dirty properties back to Glassfish via 'asadmin set'.
    def flush_dirty_properties
      set_arguments = dirty_properties.map do |glassfish_suffix, new_value|
        [to_glassfish(glassfish_suffix), new_value].join('=')
      end
      if not set_arguments.empty?
        asadmin_cmd 'set', *set_arguments
      end
    end
    
    # Generate a Glassfish property name for the passed key. By default this
    # chains to glassfish_basename, and joins the result to glassfish_suffix
    # with a dot. Override if this is an inappropriate approach.
    def to_glassfish(glassfish_suffix)
      dotted_name glassfish_basename, glassfish_suffix
    end
end
