include 'glassfish'

file { '/tmp/example':
  ensure  => file,
  owner   => glassfish,
  group   => glassfish,
  mode    => 0440,
  content => "AS_ADMIN_PASSWORD=hello\n",
}

glassfish_domain { 'example':
  ensure       => present,
  running      => true,
  # debug        => true,
  passwordfile => '/tmp/example',
  user         => 'glassfish',
  group        => 'glassfish',
  portbase     => 4800,
  require      => Class['glassfish'],
}

glassfish_network_listener { 'http-listener-1':
  ensure => absent,
  before => Glassfish_protocol['http-listener-1'],
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_protocol { 'http-listener-1':
  ensure       => absent,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_network_listener { 'http-listener-2':
  ensure => absent,
  before => Glassfish_protocol['http-listener-2'],
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_protocol { 'http-listener-2':
  ensure       => absent,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_protocol { 'ajp-listener':
  ensure       => present,
  security     => false,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_http { 'ajp-listener':
  ensure             => present,
  websockets_support => true,
  uri_encoding       => 'UTF-8',
  passwordfile       => '/tmp/example',
  require            => Glassfish_domain['example'],
}

glassfish_network_listener { 'ajp-listener':
  ensure   => present,
  protocol => 'ajp-listener',
  port     => 8000,
  address  => '127.0.0.1',
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_iiop_listener { 'SSL_MUTUALAUTH':
  ensure   => absent,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_iiop_listener { 'SSL':
  ensure   => absent,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}

glassfish_iiop_listener { 'orb-listener-1':
  ensure   => absent,
  passwordfile => '/tmp/example',
  require      => Glassfish_domain['example'],
}
