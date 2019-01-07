define ldap::modify($ldif=false) {
  if $ldif {
    $ldif_file = $ldif
  } else {
    $ldif_file = "/etc/ldap/ldifs/$name"
  }

  exec { "ldapmodify ${ldif_file}":
    command     => "ldapmodify -Y EXTERNAL -H ldapi:/// -f ${ldif_file}",
    user        => root,
    path        => "/usr/bin:/bin",
    require     => [
      Package["ldap-utils"],
      Service["slapd"],
    ],
    subscribe   => [
      File[$ldif_file],
    ],
    refreshonly => true,
  }
}
