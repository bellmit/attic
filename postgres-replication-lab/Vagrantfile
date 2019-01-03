# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box     = "ubuntu-precise-amd64"
  config.vm.box_url = "https://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-amd64-vagrant-disk1.box"

  config.vm.provider :virtualbox do |v|
    v.customize [
      "modifyvm", :id,
      "--memory", "256",
      "--natdnshostresolver1", "on"
    ]
  end

  config.vm.provision :puppet do |puppet|
    puppet.module_path = 'modules'
  end

  [:alpha, :beta, :delta].each_with_index do |node, index|
    ip = 20 + index
    config.vm.define node do |node_config|
      node_config.vm.hostname = node.to_s
      node_config.vm.network :private_network, ip: "192.168.50.#{ip}"
    end
  end
end
