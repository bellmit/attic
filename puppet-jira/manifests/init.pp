class jira {
  include 'jira::user'
  include 'jira::install'
  include 'jira::database'
  include 'jira::service'
  include 'jira::apache'
}