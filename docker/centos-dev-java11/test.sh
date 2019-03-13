#!/bin/bash
# Original author: Michael Vorburger.ch
# Originally inspired by https://github.com/fabric8io-images/s2i/blob/master/test.sh

set -ex

docker build container/ -t quarkus-dev

container_id=$(docker run --name quarkus-dev-test -d --rm -p 8080 quarkus-dev)
http_port="$(docker port ${container_id} 8080 | sed 's/0.0.0.0://')"
until $(curl --output /dev/null --silent --head --fail http://localhost:${http_port}); do sleep 1; done

http_reply=$(curl --silent --show-error http://localhost:${http_port}/hello/greeting/quarkus)
if [ "$http_reply" = 'hello quarkus' ]; then
    echo "TEST PASSED"
    docker rm -f ${container_id}
    exit 0
  else
    echo "TEST FAILED"
    docker logs ${container_id}
    docker rm -f ${container_id}
    exit -123
fi

# TODO add testing for docker -v:z -u ...
