# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # Get Ubuntu mostly-stock VM from Vagrant Cloud
  # You can try this with ubuntu/trusty64, but they broke sshd restarts during
  # the port to upstart. Wait for that to get sorted.
  config.vm.box = "hashicorp/precise64"

  config.vm.hostname = "vagrant.example.com"
  config.vm.network "private_network", ip: "192.168.33.10"

  # If you're on a Mac, run `vagrant plugin install landrush`, uncomment the
  # following, and never edit /etc/hosts again.
  #
  # config.landrush.enable
  # config.landrush.tld = 'example.com'

  config.vm.provision "shell",
    path: "bootstrap.sh",
    args: [config.vm.hostname]
end
