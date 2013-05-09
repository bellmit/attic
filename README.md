# mysqlenv - create temporary MySQL servers

The `mysqlenv` script creates MySQL servers ("environments") on demand. These
servers can be useful for testing or development, particularly in automated
environments.

## Usage

Create new MySQL environments by running

    mysqlenv PATH

This will create a directory tree rooted at `PATH` containing a complete data
directory, relevant configuration files, and a bin directory containing
wrappers for useful MySQL commands.

The new server comes up with a randomly-selected port number, printed in the
output of `mysqlenv` and stored in `PATH/conf/my.cnf`. The server listens on
localhost only by default.

To start the newly-created MySQL server and set up `mysql` to connect to it,
source the environment's `activate` script:

    . PATH/bin/activate

This adds the environment's `bin` directory to your `PATH`, and starts the
MySQL server. You can interact with the MySQL server using `mysql`, or using
any MySQL client. When you're done, run the `deactivate-mysql` command, or end
your shell session, which will stop MySQL and remove the changes from your
environment.

## Options

The behaviour of the `mysqlenv` script can be customized using any or all of
the following options:

* `-v` will enable more verbose debug output.
* `-m MYSQL_BASE` will tell mysqlenv where to look for your MySQL binaries, if
  they're not on `PATH` already.
* `-c FILE` lets you provide an alternate `my.cnf` file.
* `-p PORT` lets you control the port number MySQL starts on.
* `-i NUMBER` sets the server ID, for replication.
