# [Quarkus.io](http://quarkus.io) Live reload `quarkus:dev` container image

## OpenShift

### OpenShift Build

    oc new-build https://github.com/quarkusio/quarkus.git --context-dir=docker/centos-dev-java11 --name quarkus-dev

### OpenShift Use

    oc new-app quarkus-native-s2i~https://github.com/quarkusio/quarkus-quickstarts --context-dir=getting-started-native --name=getting-started-native

## Local Testing

    docker build . -t quarkus-dev

    docker run --rm -it quarkus-dev bash
    docker run --rm -it -p 8080:8080 quarkus-dev

    curl http://localhost:8080/hello/greeting/quarkus
