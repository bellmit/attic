define ldap::index($database_dn, $indices, $ldifs_dir = "/etc/ldap/ldifs/indices") {
  file { "${ldifs_dir}/${name}":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/index.ldif.erb"),
  }

  ldap::modify { "${ldifs_dir}/${name}":
    ldif    => "${ldifs_dir}/${name}",
    require => [
      File["${ldifs_dir}/${name}"],
    ],
  }
}
