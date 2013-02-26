require 'puppet/provider/asadmin'
require 'puppet/provider/asadmin_properties'

Puppet::Type.type(:glassfish_network_listener).provide(
  :asadmin,
  :parent => Puppet::Provider::Asadmin
) do
  include Puppet::Provider::AsadminProperties
  commands :asadmin => 'asadmin'
  
  mk_resource_methods
  
  private
    def destroy!
      asadmin_cmd 'delete-network-listener', name
    end
    
    def create!
      create_args = []
      create_args << '--protocol' << resource[:protocol]
      create_args << '--address' << resource[:address]
      create_args << '--port' << resource[:port]
      create_args << '--jkenabled' if resource[:ajp] == :true
      create_args << name
      asadmin_cmd 'create-network-listener', *create_args
      is(:protocol, resource[:protocol])
      is(:address, resource[:address])
      is(:port, resource[:port])
      is(:ajp, resource[:ajp])
    end
    
    def self.glassfish_properties
      {
        'protocol'   => :protocol,
        'address'    => :address,
        'port'       => :port,
        'jk-enabled' => :ajp,
      }
    end
    
    def self.glassfish_search_base
      'server.network-config.network-listeners.network-listener'
    end
end
