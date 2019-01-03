class rabbitmq::absent {
  service { 'rabbitmq-server':
    ensure     => stopped,
    enable     => false,
    hasrestart => true,
    before     => Package['rabbitmq-server'],
  }

  package { 'rabbitmq-server':
    ensure => absent,
    before => Package['erlang'],
  }

  package { 'erlang':
    ensure => absent,
  }
}