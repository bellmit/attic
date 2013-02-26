class rabbitmq::present(
  $version        = '2.8.1-1',
  $erlang_version = 'R14B-03.3.el5',
  $ensure         = 'running', # 'present', 'stopped', 'running'
  $enable         = true
) {

  package { 'erlang':
    ensure => $erlang_version,
  }

  package { 'rabbitmq-server':
    ensure  => $version,
    require => Package['erlang'],
  }

  $service_ensure = $ensure ? {
    'present' => undef,   # "present" implies "don't start or stop the service".
    default   => $ensure,
  }

  service { 'rabbitmq-server':
    ensure     => $service_ensure,
    enable     => $enable,
    hasrestart => true,
    require    => Package['rabbitmq-server'],
  }
}
