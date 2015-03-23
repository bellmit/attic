FROM ubuntu:14.04

RUN apt-get update && \
    apt-get install -y znc && \
    apt-get clean

RUN adduser --system --home /znc znc

WORKDIR /znc

COPY configs/ .znc/configs/
RUN chown -R znc: .

VOLUME /znc/.znc

USER znc

EXPOSE 7000
CMD znc -f
