require 'puppet/type/glassfish'

Puppet::Type.newtype(:glassfish_http) do
  include Puppet::Type::Glassfish
  
  @doc = "Creates and manages Glassfish HTTP protocol configurations."
  
  newparam(:name) do
    desc "Underlying protocol name"
    isnamevar
    isrequired
  end
  
  ensurable do
    defaultvalues
    defaultto :present
  end
  
  newproperty(:default_virtual_server) do
    desc "Default virtual server for requests that do not provide a Host: header matching another virtual server."
    newvalues(/.*/)
    defaultto 'server'
  end
  
  newproperty(:dns_lookup, :boolean => true) do
    desc "Configures reverse DNS lookups for incoming clients"
    newvalues(:true, :false)
    defaultto :false
    isrequired
  end
  
  newproperty(:websockets_support, :boolean => true) do
    desc "Configures WebSocket support for this protocol"
    newvalues(:true, :false)
    defaultto :false
    isrequired
  end
  
  newproperty(:uri_encoding) do
    desc "Encoding URIs will be parsed under on this protocol."
    newvalues(/.*/)
    defaultto 'UTF-8'
    isrequired
  end
  
  autorequire :glassfish_protocol do
    self[:name] if self[:ensure] == :present
  end
end
