module Puppet::Type::Glassfish
  def self.included(into)
    into.instance_eval do
      newparam(:admin_user) do
        desc "Glassfish admin user name (default: 'admin')"
        defaultto "admin"
      end
      
      newparam(:passwordfile) do
        desc "Glassfish password file (if not provided, commands will use no password)"
      end
      
      newparam(:user) do
        desc "OS user to run Glassfish commands as (defaults to the puppet user)"
      end
      
      newparam(:group) do
        desc "OS user to run Glassfish commands as (defaults to the group of the `user` property)"
      end
      
      autorequire :user do
        self[:user]
      end
      
      autorequire :group do
        self[:group]
      end
      
      autorequire :file do
        self[:passwordfile]
      end
    end
  end
end
