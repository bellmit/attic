define ldap::login($ldap_uri, $search_base, $bind_dn, $bind_passwd) {
  package { "libnss-ldap":
    ensure => latest,
  }

  package { "libpam-ldap":
    ensure => latest,
  }

  file { "/etc/ldap.conf":
    owner   => root,
    group   => root,
    mode    => 444,
    content => template("ldap/ldap.conf.erb"),
  }

  exec { "auth-client-config -p lac_ldap -t nss":
    user    => root,
    path    => "/usr/sbin:/sbin:/usr/bin:/bin",
    refreshonly => true,
    subscribe => [
      Package["libnss-ldap"],
    ],
  }

  exec { "pam-auth-update --package (for ldap::login)":
    command => "pam-auth-update --package",
    user    => root,
    path    => "/usr/sbin:/sbin:/usr/bin:/bin",
    refreshonly => true,
    subscribe => [
      Package["libpam-ldap"],
    ],
  }
}