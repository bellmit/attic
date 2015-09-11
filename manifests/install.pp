class jira::install(
  $version = '6.3.8'
) {
  require 'atlassian'
  require 'jira::user'

  $download_url = "http://www.atlassian.com/software/jira/downloads/binary/atlassian-jira-${version}.tar.gz"
  $current      = "${atlassian::install_base}/jira/current"

  file { "${atlassian::install_base}/jira":
    ensure  => directory,
    owner   => jira,
    group   => jira,
    mode    => 0644,
  }

  staging::deploy { "atlassian-jira-${version}.tar.gz":
    source  => $download_url,
    target  => "${atlassian::install_base}/jira",
    creates => "${atlassian::install_base}/jira/atlassian-jira-${version}-standalone",
    timeout => 900,
    user    => jira,
    group   => jira,
  }

  file { $current:
    ensure  => link,
    target  => "${atlassian::install_base}/jira/atlassian-jira-${version}-standalone",
    owner   => root,
    group   => root,
    require => Staging::Deploy["atlassian-jira-${version}.tar.gz"],
  }
}
