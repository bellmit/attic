class Puppet::Provider::Asadmin < Puppet::Provider
  def self.asadmin_cmd(*args)
    opts = {}
    if args[-1].is_a? Hash
      opts = args[-1]
      args = args[0...-1]
    end
    
    asadmin_args = []
    asadmin_args << '--user' << opts[:admin_user] if opts[:admin_user]
    asadmin_args << '--passwordfile' << opts[:passwordfile] if opts[:passwordfile]
    
    Puppet::Util::SUIDManager.asuser(opts[:user], opts[:group]) do
      original_uid = Process.uid
      original_gid = Process.gid
      begin
        # drop privs even harder -- bash (and thus asadmin) restore EUID to
        # UID. We can resume our original privs (root) afterwards.
        Process.uid = Process.euid
        Process.gid = Process.egid
        asadmin '--terse', *(asadmin_args + args)
      ensure
        Process.uid = original_uid
        Process.gid = original_gid
      end
    end
  end
  
  def asadmin_cmd(*args)
    opts = {}
    if args[-1].is_a? Hash
      opts = args[-1]
      args = args[0...-1]
    end
    
    default_opts = {
      :admin_user   => resource[:admin_user],
      :passwordfile => resource[:passwordfile],
      :user  => resource[:user],
      :group => resource[:group],
    }
    opts = opts.merge(default_opts)
    
    self.class.asadmin_cmd *(args + [opts])
  end
end
