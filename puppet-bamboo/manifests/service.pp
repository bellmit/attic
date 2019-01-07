class bamboo::service(
  $ensure = 'running',
  $enable = true
) {
  require 'bamboo::config'
  require 'bamboo::capabilities'

  require 'java'

  file { '/etc/init/bamboo.conf':
    ensure  => file,
    owner   => root,
    group   => root,
    mode    => 0444,
    content => template('bamboo/etc/init/bamboo.conf.erb'),
  }

  service { 'bamboo':
    ensure    => $ensure,
    enable    => $enable,
    provider  => upstart,
    subscribe => File['/etc/init/bamboo.conf'],
  }
}