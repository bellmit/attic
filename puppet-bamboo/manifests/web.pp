class bamboo::web(
  $hostname = $::fqdn
) {
  apache2::site { $hostname:
    config => template('bamboo/apache.conf.erb'),
  }
}