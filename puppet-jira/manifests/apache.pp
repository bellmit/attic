class jira::apache(
  $hostname = $::fqdn
) {
  apache2::site { $hostname:
    config => template('jira/apache.conf.erb'),
  }
}