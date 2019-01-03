require 'puppet/provider/asadmin'
require 'puppet/provider/asadmin_properties'

Puppet::Type.type(:glassfish_iiop_listener).provide(
  :asadmin,
  :parent => Puppet::Provider::Asadmin
) do
  include Puppet::Provider::AsadminProperties
  commands :asadmin => 'asadmin'
  
  mk_resource_methods
  
  private
    def destroy!
      asadmin_cmd 'delete-iiop-listener', name
    end
    
    def create!
      create_args = []
      create_args << '--listeneraddress' << resource[:address]
      create_args << '--iiopport' << resource[:port]
      create_args << '--securityenabled' if resource[:security] == :true
      create_args << name
      asadmin_cmd 'create-iiop-listener', *create_args
      is(:address, resource[:address])
      is(:port, resource[:port])
      is(:security, resource[:security])
    end
    
    def self.glassfish_properties
      {
        'address'          => :address,
        'port'             => :port,
        'security-enabled' => :security,
      }
    end
    
    def self.glassfish_search_base
      'server.iiop-service.iiop-listener'
    end
end
