Puppet::Type.newtype(:rabbitmq_permission) do
  @doc = "Creates and manages RabbitMQ permissions"
  
  newparam(:name) do
    desc "Arbitrary, human-readable name for this permission (optional; defaults to `user (vhost)`.)"
  end
  
  newproperty(:user) do
    desc "User name to manage"
    isnamevar
  end
  
  ensurable
  
  # using a property here would cause puppet to log the password - no good.
  newproperty(:vhost) do
    desc "The RabbitMQ vhost to control permissions in"
    defaultto '/'
  end
  
  newproperty(:config_match) do
    desc "Regex matching the objects this user may configure."
  end
  
  newproperty(:read_match) do
    desc "Regex matching the objects this user may read from."
  end
  
  newproperty(:write_match) do
    desc "Regex matching the objects this user may write to."
  end
  
  autorequire(:service) { ['rabbitmq-server'] }
  autorequire(:rabbitmq_user) { self[:user] if self[:ensure] == :present }
  autorequire(:rabbitmq_vhost) { self[:vhost] if self[:ensure] == :present }
end
