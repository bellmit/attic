require 'puppet/type/glassfish'

Puppet::Type.newtype(:glassfish_protocol) do
  include Puppet::Type::Glassfish
  
  @doc = "Creates and manages Glassfish network protocols."
  
  newparam(:name) do
    desc "Protocol name"
    isnamevar
  end
  
  ensurable do
    defaultvalues
    defaultto :present
  end
  
  newproperty(:security, :boolean => true) do
    desc "Enable (or disable) SSL on this protocol"
    newvalues(:true, :false)
    defaultto :false
  end
  
  autorequire :glassfish_http do
    self[:name] if self[:ensure] == :absent
  end
end
