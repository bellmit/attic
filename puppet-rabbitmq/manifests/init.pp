class rabbitmq(
  $version        = '2.8.1-1',
  $erlang_version = 'R14B-03.3.el5',
  $ensure         = 'running', # 'absent', 'present', 'stopped', 'running'
  $enable         = true
) {
  if $ensure == 'absent' {
    class { 'rabbitmq::absent':
    }
  } else {
    class { 'rabbitmq::present':
      version        => $version,
      erlang_version => $erlang_version,
      ensure         => $ensure,
      enable         => $enable,
    }
  }
}
