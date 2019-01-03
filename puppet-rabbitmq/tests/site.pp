include 'rabbitmq'

rabbitmq_vhost { '/':
  ensure => present,
}

rabbitmq_vhost { '/examples':
  ensure => present,
}

rabbitmq_user { 'guest':
  ensure   => present,
}

rabbitmq_user { 'examples':
  ensure   => present,
  password => 'PASSWORD',
}

rabbitmq_permission { 'guest (/)':
  ensure       => present,
  user         => guest,
  vhost        => '/',
  read_match   => '.*',
  write_match  => '.*',
  config_match => '.*',
}

rabbitmq_permission { 'guest (/examples)':
  ensure       => present,
  user         => guest,
  vhost        => '/examples',
  read_match   => 'a|b',
  write_match  => 'x|y',
}
