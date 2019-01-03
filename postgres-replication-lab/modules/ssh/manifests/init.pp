class ssh {
  service { 'ssh':
    ensure => running,
    enable => true,
  }
}