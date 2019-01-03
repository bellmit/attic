require 'puppet/provider/asadmin'
require 'puppet/provider/asadmin_utils'

Puppet::Type.type(:glassfish_domain).provide(
  :asadmin,
  :parent => Puppet::Provider::Asadmin
) do
  extend Puppet::Provider::AsadminUtils
  
  commands :asadmin => 'asadmin'
  
  mk_resource_methods
  
  self::NOT_RUNNING = %r{(.*) not running$}
  self::RUNNING = %r{(.*) running$}
  self::FIELDS = [:name]
  
  def self.list_domains
    (asadmin_cmd 'list-domains').lines.each do |domain_desc|
      if match = self::NOT_RUNNING.match(domain_desc)
        fields = parse_domain_desc(match)
        yield fields.merge :running => :false
      elsif match = self::RUNNING.match(domain_desc)
        fields = parse_domain_desc(match)
        yield fields.merge :running => :true
      else
        Puppet.warning "Failed parsing domain description: #{domain_desc}"
      end
    end
  end
  
  def self.instances
    domains = []
    list_domains do |fields|
      domains << new(fields)
    end
    domains
  end
  
  def self.parse_domain_desc(match)
    fields = {:ensure => :present}
    self::FIELDS.zip(match.captures).each do |field, value|
      fields[field] = value
    end
    fields
  end
  
  def self.prefetch(resources)
    instances_by_name = by_key(instances) { |instance| instance.name }
    resources.each do |name, resource|
      if !(provider = instances_by_name[name]).nil?
        resource.provider = provider
      else
        resource.provider = instances_by_name[name] = new(
          :name   => name,
          :ensure => :absent
        )
      end
    end
  end
  
  def exists?
    @property_hash[:ensure] == :present
  end
  
  def create
    args = []
    args << 'create-domain'
    args << '--nopassword' unless resource[:passwordfile]
    args << '--portbase' << resource[:portbase] if resource[:portbase]
    args << name
    asadmin_cmd *args
    start if resource[:running] != :false
  end
  
  def destroy
    stop if running != :false
    asadmin_cmd 'delete-domain', name
  end
  
  def running=(should_run)
    if should_run == :true and running != :true
      start
    elsif should_run == :false and running != :false
      stop
    end
  end
  
  def start
    args = []
    args << 'start-domain'
    args << '--debug' if resource[:debug] == :true
    args << name
    asadmin_cmd *args if running != :true
    @property_hash[:running] = :true
  end
  
  def stop
    asadmin_cmd 'stop-domain', name if running != :false
    @property_hash[:running] = :false
  end
  
  def restart
    args = []
    args << 'restart-domain'
    args << '--debug' if resource[:debug] == :true
    args << name
    asadmin_cmd *args
    @property_hash[:running] = :true
  end
end
