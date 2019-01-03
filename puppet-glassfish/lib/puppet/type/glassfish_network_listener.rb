require 'puppet/type/glassfish'

Puppet::Type.newtype(:glassfish_network_listener) do
  include Puppet::Type::Glassfish
  
  @doc = "Creates and manages Glassfish network listeners."
  
  newparam(:name) do
    desc "Listener name"
    isnamevar
  end
  
  ensurable do
    defaultvalues
    defaultto :present
  end
  
  newproperty(:address) do
    desc "IP address to listen on"
    defaultto '0.0.0.0'
    isrequired
  end
  
  newproperty(:port) do
    desc "Network port to listen on"
    isrequired
  end
  
  newproperty(:protocol) do
    desc "Network protocol to listen for"
    isrequired
  end
  
  newproperty(:ajp, :boolean => true) do
    desc "Tunnel underlying protocol over AJP"
    newvalues(:true, :false)
    defaultto :false
  end
  
  autorequire :glassfish_protocol do
    self[:protocol] if self[:ensure] == :present
  end
end
