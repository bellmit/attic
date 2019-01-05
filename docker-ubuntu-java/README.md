# Docker image for Java on Ubuntu

[![Build Status](https://circleci.com/gh/login-box/docker-ubuntu-java.svg?style=shield&circle-token=28b90bc76f8ea447b92bbfe2db7ff9415113207c)](https://circleci.com/gh/login-box/docker-ubuntu-java)

**You must build these images for yourself.** They rely on the Oracle JDK
packages; you'll have to decide for yourself whether to accept their license
agreement.

## To Build

`docker build --tag MYNAME/ubuntu-java .`

## Contents

* Ubuntu 14.04 LTS (from `ubuntu:14.04` at the time of packaging)
* Oracle Java 8 JDK (from [the webupd8 team's PPA](http://www.webupd8.org/p/ubuntu-ppas-by-webupd8.html))
* The `JAVA_OPTS` enviroment variable, which is set to `-Xmx512m` by default. (Override this in your own Dockerfile, or at startup with `--env`/`--env-file`)
