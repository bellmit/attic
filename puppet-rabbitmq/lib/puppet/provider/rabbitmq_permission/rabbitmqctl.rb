require 'puppet/provider/rabbitmqctl'

Puppet::Type.type(:rabbitmq_permission).provide(
  :rabbitmqctl,
  :parent => Puppet::Provider::Rabbitmqctl
) do
  commands :rabbitmqctl => 'rabbitmqctl'
  
  mk_resource_methods
  
  self::PERMS_FIELDS = [:user, :config_match, :write_match, :read_match]
  
  def self.instances
    return rabbitmqctl_cmd('list_vhosts').lines.map do |line|
      vhost = line.chomp
      rabbitmqctl_cmd('list_permissions', '-p', vhost).lines.map do |perms_desc|
        properties = {
          :vhost  => vhost,
          :ensure => :present,
        }
        values = perms_desc.chomp.split "\t"
        self::PERMS_FIELDS.zip(values).each do |field, value|
          properties[field] = value
        end
        properties[:name] = "#{properties[:user]} (#{properties[:vhost]})"
        
        new properties
      end
    end.flatten
  end
  
  def self.prefetch(resources)
    perms_by_holder = by_key(instances) { |instance| [instance.user, instance.vhost] }
    resources.each do |name, resource|
      if (provider = perms_by_holder[[resource[:user], resource[:vhost]]]).nil?
        resource.provider = perms_by_holder[[resource[:user], resource[:vhost]]] = new(
          :name   => resource.name,
          :user   => resource[:user],
          :vhost  => resource[:vhost],
          :ensure => :absent,
          :config_match => '',
          :write_match  => '',
          :read_match   => ''
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
    self.config_match = resource[:config_match]
    self.write_match = resource[:write_match]
    self.read_match = resource[:read_match]
  end
  
  def destroy
    @property_hash[:ensure] = :absent
  end
  
  def perms_or_absent(v)
    return '' if v == :absent
    return v
  end
  
  def flush
    if @property_hash[:ensure] == :present
      config = perms_or_absent config_match
      write  = perms_or_absent write_match
      read   = perms_or_absent read_match
      if @property_was[:ensure] == :absent or
        @property_was[:config_match] != config or
        @property_was[:write_match] != write or
        @property_was[:read_match] != read
        
        rabbitmqctl_cmd 'set_permissions', '-p', vhost, user, config, write, read
      end
    else
      if @property_was[:ensure] == :present
        rabbitmqctl_cmd 'clear_permissions', '-p', vhost, user
      end
    end
  end
end
