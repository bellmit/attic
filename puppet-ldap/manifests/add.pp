define ldap::add($base, $filter, $ldif=false, $attributes="dn", $expect="^dn\\:") {
  if $ldif {
    $ldif_file = $ldif
  } else {
    $ldif_file = "/etc/ldap/ldifs/$name"
  }

  exec { "ldapadd ${ldif_file}":
    command => "ldapadd -Y EXTERNAL -H ldapi:/// -f ${ldif_file}",
    user    => root,
    path    => "/usr/bin:/bin",
    unless  => "ldapsearch -LLL -Q -Y EXTERNAL -H ldapi:/// -b '${base}' '${filter}' '${attributes}' | grep '${expect}'",
    require => [
      Package["ldap-utils"],
      Service["slapd"],
    ],
  }
}
