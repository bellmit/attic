require 'puppet/provider/rabbitmqctl'

Puppet::Type.type(:rabbitmq_user).provide(
  :rabbitmqctl,
  :parent => Puppet::Provider::Rabbitmqctl
) do
  commands :rabbitmqctl => 'rabbitmqctl'
  
  mk_resource_methods
  
  self::USER_FIELDS = [:name, :tags]
  
  def self.instances
    return rabbitmqctl_cmd('list_users').lines.map do |line|
      fields = line.chomp.split "\t"
      properties = {:ensure => :present}
      self::USER_FIELDS.zip(fields).each do |field, value|
        properties[field] = munge_property(field, value)
      end
      new properties
    end
  end
  
  def self.munge_property(name, value)
    if respond_to? "do_munge_#{name}"
      send "do_munge_#{name}", value
    else
      value
    end
  end
  
  def self.do_munge_tags(tags_desc)
    tags_desc = tags_desc[1...-1] # strip []
    tags_desc.split(", ")
  end
  
  def self.prefetch(resources)
    instances_by_name = by_name(instances)
    resources.each do |name, resource|
      if (provider = instances_by_name[name]).nil?
        resource.provider = instances_by_name[name] = new(
          :name   => name,
          :ensure => :absent
        )
      else
        resource.provider = provider
      end
    end
  end
  
  def initialize(properties)
    @property_was = properties.dup
    super(properties)
  end
  
  def exists?
    @property_hash[:ensure] == :present
  end
  
  def create
    @property_hash[:ensure] = :present
  end
  
  def destroy
    @property_hash[:ensure] = :absent
  end
  
  def flush
    if @property_hash[:ensure] == :present
      if @property_was[:ensure] == :absent
        new_password = password
        new_password = name if new_password == :absent
        rabbitmqctl_cmd 'add_user', name, new_password
      else
        if password != :absent
          # Do this blindly, as we can't tell whether the user's current p/w
          # matches their intended p/w.
          rabbitmqctl_cmd 'change_password', name, password
        end
      end
      if tags != @property_was[:tags] and tags != :absent
        rabbitmqctl_cmd 'set_user_tags', name, *tags
      end
    elsif @property_hash[:ensure] == :absent and @property_was[:ensure] == :present
      rabbitmqctl_cmd 'delete_user', name
    end
  end
end
