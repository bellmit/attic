require 'puppet/provider/asadmin'
require 'puppet/provider/asadmin_properties'

Puppet::Type.type(:glassfish_http).provide(
  :asadmin,
  :parent => Puppet::Provider::Asadmin
) do
  include Puppet::Provider::AsadminProperties
  commands :asadmin => 'asadmin'
  
  mk_resource_methods
  
  private
    def destroy!
      asadmin_cmd 'delete-http', name
    end
    
    def create!
      create_args = []
      create_args << '--default-virtual-server' << resource[:default_virtual_server]
      create_args << '--dns-lookup-enabled' if resource[:dns_lookup] == :true
      create_args << name
      asadmin_cmd 'create-http', *create_args
      is(:default_virtual_server, resource[:default_virtual_server])
      is(:dns_lookup, resource[:dns_lookup])
    end
    
    def to_glassfish(glassfish_suffix)
      dotted_name(
        'server.network-config.protocols.protocol',
        name,
        'http',
        glassfish_suffix
      )
    end
    
    def self.glassfish_properties
      {
        'default-virtual-server'     => :default_virtual_server,
        'dns-lookup-enabled'         => :dns_lookup,
        'websockets-support-enabled' => :websockets_support,
        'uri-encoding'               => :uri_encoding,
      }
    end
    
    def self.glassfish_search_base
      'server.network-config.protocols.protocol.*.http'
    end
    
    def self.match_descriptor(base, descriptor)
      pattern = /#{Regexp.quote('server.network-config.protocols.protocol.')}([^.]*)#{Regexp.quote('.http.')}([^=]*)=(.*)/
      pattern.match(descriptor)
    end
end
