# vim: set filetype=dockerfile :
FROM gliderlabs/alpine

RUN apk-install autoconf automake gettext-dev \
    g++ make openssl-dev pkgconfig zlib-dev \
  && mkdir /src \
  && cd /src \
  && wget http://znc.in/releases/znc-1.6.0.tar.gz \
  && tar -xzvf znc-1.6.0.tar.gz \
  && cd znc-1.6.0 \
  && ./configure \
  && make \
  && make install \
  && cd / \
  && rm -rf /src \
  && apk del --purge autoconf automake gettext-dev \
    g++ make openssl-dev pkgconfig zlib-dev \
  && apk-install libstdc++

RUN adduser -S -h /znc znc

WORKDIR /znc

COPY configs/ .znc/configs/
RUN chown -R znc: .

VOLUME /znc/.znc

USER znc

EXPOSE 7000
CMD znc -f
