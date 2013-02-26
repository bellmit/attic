require 'puppet/provider/rabbitmqctl'

Puppet::Type.type(:rabbitmq_vhost).provide(
  :rabbitmqctl,
  :parent => Puppet::Provider::Rabbitmqctl
) do
  commands :rabbitmqctl => 'rabbitmqctl'
  
  mk_resource_methods
  
  def self.instances
    return rabbitmqctl_cmd('list_vhosts').lines.map do |line|
      new :name => line.chomp, :ensure => :present
    end
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
    if @property_was[:ensure] == :absent and @property_hash[:ensure] == :present
      rabbitmqctl_cmd 'add_vhost', name
    elsif @property_was[:ensure] == :present and @property_hash[:ensure] == :absent
      rabbitmqctl_cmd 'delete_vhost', name
    end
  end
end
