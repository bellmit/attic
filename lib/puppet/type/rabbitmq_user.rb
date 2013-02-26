Puppet::Type.newtype(:rabbitmq_user) do
  @doc = "Creates and manages RabbitMQ users"
  
  # This is implemented this way so that Puppet _always_ sets the password if
  # the password is being managed; in turn, this ensures that password changes
  # propagate into RabbitMQ.
  class PasswordParam < Puppet::Property
    def should_to_s(should)
      # Do not print passwords in Puppet logs, ok?
      '********'
    end
    
    def is_to_s(is)
      # Do not print passwords in Puppet logs, ok?
      '********'
    end
    
    def insync?(is)
      should.nil?
    end
  end
  
  class ArrayParam < Puppet::Property
    def should=(value)
      @should = value
    end
    
    def should
      @should
    end
    
    def retrieve
      v = provider.send(name) or :absent
      v
    end
    
    def insync?(is)
      is == should
    end
  end
  
  newparam(:name) do
    desc "User name"
    isnamevar
  end
  
  ensurable
  
  newproperty(:password, :parent => PasswordParam) do
    desc <<-DOC
      The plain text password of the RabbitMQ user. If not set, Puppet will
      not manage the password.
      
      If unset when puppet creates a new user, Puppet will set a default
      password equal to the user's name - this is not very secure."
    DOC
    newvalues(/./)
  end
  
  newproperty(:tags, :parent => ArrayParam) do
    desc <<-DOC
      The user's RabbitMQ tags. If not set, Puppet will not manage tags; to
      remove a user's tags, set this to an empty list.
    DOC
  end
  
  autorequire(:service) { ['rabbitmq-server'] }
end
