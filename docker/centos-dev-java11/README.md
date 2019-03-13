# [Quarkus.io](http://quarkus.io) Live reload `quarkus:dev` container image

This guide was originally contributed by Michael Vorburger.

## Kubernetes

TODO

## OpenShift

### OpenShift Build (one time)

    oc new-build https://github.com/quarkusio/quarkus.git --context-dir=docker/centos-dev-java11 --name quarkus-dev
    oc logs -f bc/quarkus-dev

### OpenShift Use (every time)

    oc new-app quarkus-dev --name=demo-incluster-live-reload
    oc expose svc/demo-incluster-live-reload

    git clone https://github.com/quarkusio/quarkus-quickstarts
    cd quarkus-quickstarts/getting-started
    mvn clean
    DEV_POD=$(TODO)
    oc rsync . DEV_POD:/home/quarkus-dev/

    sed -i 's/Your new/Your live reloading new/' src/main/resources/META-INF/resources/index.html
    curl http://localhost:8080

    sed -i 's/hello /hello live reloading /' src/main/java/org/acme/quickstart/GreetingService.java
    curl http://localhost:8080/hello/greeting/quarkus

## Local Dev without Java & Maven using Docker

You can map local code into this container, just to avoiding having to have a JVM and Maven installed, like so:

    git clone https://github.com/quarkusio/quarkus-quickstarts
    cd quarkus-quickstarts/getting-started

    docker run --rm -it -p 8080:8080 -v $(pwd):/home/quarkus-dev/:z -u `id -u $USER`:`id -g $USER` quarkus-dev

NB: The `z` suffix -v mount flag and `-u` option are required to correctly map the file ownership in/out of the container.

## Local Testing (only for development of this container itself)

    docker build . -t quarkus-dev

    docker run --rm -it quarkus-dev bash
    docker run --rm -it -p 8080:8080 quarkus-dev

    curl http://localhost:8080/hello/greeting/quarkus
