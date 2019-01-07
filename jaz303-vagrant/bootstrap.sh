#!/bin/bash -e

HOSTNAME="$(echo "$1" | cut -d '.' -f 1)"
hostname "${HOSTNAME}"
echo "127.0.1.1 ${HOSTNAME} $1" >> /etc/hosts

#
# Ansible user

adduser --disabled-password --gecos "" ansible
usermod -a -G sudo ansible
usermod -a -G ssh ansible

#
# Vagrant user
usermod -a -G ssh vagrant

#
# SSH key for ansible user

mkdir ~ansible/.ssh
cp /vagrant/keys/ansible.pub ~ansible/.ssh/authorized_keys
chown -R ansible:ansible ~ansible/.ssh
chmod 700 ~ansible/.ssh
chmod 600 ~ansible/.ssh/authorized_keys

#
# Update sudoers

cp /etc/sudoers sudoers.tmp
sed -i '/%sudo/c\%sudo ALL=(ALL:ALL) NOPASSWD: ALL' sudoers.tmp
visudo -c -f sudoers.tmp && mv sudoers.tmp /etc/sudoers

#
# Lock down SSH

sed -i '/PermitRootLogin/d' /etc/ssh/sshd_config
sed -i '/AllowGroups/d' /etc/ssh/sshd_config
sed -i '/PasswordAuthentication/d' /etc/ssh/sshd_config

echo "PermitRootLogin no" >> /etc/ssh/sshd_config
echo "AllowGroups ssh" >> /etc/ssh/sshd_config
echo "PasswordAuthentication no" >> /etc/ssh/sshd_config

service ssh restart
