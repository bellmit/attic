FROM ubuntu:14.04

RUN export DEBIAN_FRONTEND=noninteractive && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:webupd8team/java && \
    apt-get purge -y software-properties-common && \
    apt-get autoremove -y --purge && \
    apt-get clean

RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    apt-get clean

ENV JAVA_OPTS -Xmx512m
