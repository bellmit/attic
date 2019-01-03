require 'puppet/provider/asadmin'
require 'puppet/provider/asadmin_properties'

Puppet::Type.type(:glassfish_protocol).provide(
  :asadmin,
  :parent => Puppet::Provider::Asadmin
) do
  include Puppet::Provider::AsadminProperties
  commands :asadmin => 'asadmin'
  
  mk_resource_methods
  
  private
    def destroy!
      asadmin_cmd 'delete-protocol', name
    end
    
    def create!
      create_args = []
      create_args << '--securityenabled' if resource[:security] == :true
      create_args << name
      asadmin_cmd 'create-protocol', *create_args
      is(:security, resource[:security])
    end
    
    def self.glassfish_properties
      {
        'security-enabled' => :security
      }
    end
    
    def self.glassfish_search_base
      'server.network-config.protocols.protocol'
    end
end
