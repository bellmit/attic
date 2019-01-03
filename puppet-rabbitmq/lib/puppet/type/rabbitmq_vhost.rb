Puppet::Type.newtype(:rabbitmq_vhost) do
  @doc = "Creates and manages RabbitMQ virtualhosts"
  
  newparam(:name) do
    desc "VHost name"
    isnamevar
  end
  
  ensurable
  
  autorequire(:service) { ['rabbitmq-server'] }
end
