require 'puppet/type/glassfish'

Puppet::Type.newtype(:glassfish_iiop_listener) do
  include Puppet::Type::Glassfish
  
  @doc = "Creates and manages Glassfish IIOP listeners."
  
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
  
  newproperty(:security, :boolean => true) do
    desc "Enable (or disable) SSL on this protocol"
    newvalues(:true, :false)
    defaultto :false
  end
end
