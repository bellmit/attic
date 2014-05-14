class bamboo::install(
  $version = '5.5.0'
) {
  require 'bamboo::user'

  $base         = '/opt/bamboo'
  $current      = "${base}/current"
  $install_dir  = "${base}/atlassian-bamboo-${version}"
  $download_url = "http://www.atlassian.com/software/bamboo/downloads/binary/atlassian-bamboo-${version}.tar.gz"

  require 'staging'

  file { $base:
    ensure => directory,
    owner  => bamboo,
    group  => bamboo,
    mode   => '0660',
  }

  staging::deploy { "atlassian-bamboo-${version}.tar.gz":
    source  => $download_url,
    target  => $base,
    creates => $install_dir,
    timeout => 900,
    user    => bamboo,
    group   => bamboo,
    require => File[$base],
  }

  file { $current:
    ensure  => link,
    target  => $install_dir,
    owner   => bamboo,
    group   => bamboo,
    require => Staging::Deploy["atlassian-bamboo-${version}.tar.gz"],
    notify  => Service['bamboo'],
  }
}
