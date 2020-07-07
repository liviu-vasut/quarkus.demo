#!/bin/bash

cp logstash.conf /tmp/
podman run -it --rm -v /tmp/logstash.conf:/usr/share/logstash/pipeline/logstash.conf -p 12201:12201/udp docker.io/library/logstash:7.8.0

