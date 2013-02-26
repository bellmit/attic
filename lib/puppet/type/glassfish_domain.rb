require 'puppet/type/glassfish'

Puppet::Type.newtype(:glassfish_domain) do
  include Puppet::Type::Glassfish
  
  @doc = "Creates and manages Glassfish domains"
  
  newparam(:name) do
    desc "Connection pool name"
    isnamevar
  end
  
  ensurable
  
  newproperty(:running, :boolean => true) do
    desc "Controls whether puppet will keep the domain running (default: true)"
    defaultto true
    newvalues(:true, :false)
  end
  
  newparam(:debug, :boolean => true) do
    desc "Controls whether the domain will be started in debug mode"
    defaultto false
    newvalues(:true, :false)
  end
  
  newparam(:portbase) do
    desc "Port base for new domain"
    newvalues(/\d+/)
  end
  
  def refresh
    if should(:running) == :true
      provider.restart
    end
  end
end
