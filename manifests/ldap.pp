class bamboo::ldap(
  $ldap_server,
  $base_dn,
  $bind_dn,
  $bind_password
) {
  require 'bamboo::user'

  $config_dirs = [
    "${bamboo::user::home}/xml-data",
    "${bamboo::user::home}/xml-data/configuration",
  ]

  $atlassian_user = "${bamboo::user::home}/xml-data/configuration/atlassian-user.xml"

  file { $config_dirs:
    ensure => directory,
    owner  => bamboo,
    group  => bamboo,
    mode   => '0644',
  }

  file { $atlassian_user:
    ensure  => file,
    owner   => bamboo,
    group   => bamboo,
    mode    => '0644',
    content => template('bamboo/ldap/atlassian-user.xml.erb'),
    notify  => Service['bamboo'],
  }
}