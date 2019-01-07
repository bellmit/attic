class bamboo::config {
  require 'bamboo::user'
  require 'bamboo::install'

  $bamboo_home = $bamboo::user::home
  $bamboo_init = "${bamboo::install::current}/atlassian-bamboo/WEB-INF/classes/bamboo-init.properties"

  file { $bamboo_init:
    ensure  => file,
    owner   => bamboo,
    group   => bamboo,
    mode    => '0440',
    content => template('bamboo/bamboo-init.properties.erb'),
    notify  => Service['bamboo'],
  }
}