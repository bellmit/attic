# PostgreSQL Replication Lab

## What's Here

This `Vagrantfile` and related Puppet manifests sets up a three-node lab
environment for experimenting with PostgreSQL replication. Each node is
initially configured identically:

* Ubuntu 12.04 LTS (from Ubuntu's cloud images)
* PostgreSQL 9.2 (from apt.postgresql.org)
    * Network access from the 192.168.50.0/24 network (including the host
      system).
* SSH keys configured to allow SSH between the nodes
* Extremely simple scripts for wiring up replication

## Getting Started

1. Run `./mk-keys` to create host keys and the `postgres` user's SSH key.
2. Add the IP addresses of the VMs to your host system's `/etc/hosts` (see
   below).
3. Run `vagrant up` to create and configure the VMs.
4. Enjoy!

## The Included Scripts

There are two included scripts that can be used to initialize the replicas.
For example, to configure both `beta` and `delta` as streaming replicas of
`alpha`:

    root@beta# sudo -u postgres /vagrant/scripts/reset-replica alpha
    root@delta# sudo -u postgres /vagrant/scripts/reset-replica alpha

Or, to set up a replication chain from alpha through beta to delta:

    root@beta# sudo -u postgres /vagrant/scripts/reset-replica alpha
    root@delta# sudo -u postgres /vagrant/scripts/reset-replica beta alpha

The included `seed-replica` script can be used to prepare a replica prior to
running `reset-replica`. Read the scripts for details; they're short.

## `/etc/hosts`

Adding the following entries to `/etc/hosts` on your host system will make it
easier to use these VMs:

    192.168.50.20    alpha
    192.168.50.21    beta
    192.168.50.22    delta

The `Vagrantfile` includes instructions to Virtualbox to use the host
resolver, so these hosts entries will also provide name resolution for
inter-VM communication.
