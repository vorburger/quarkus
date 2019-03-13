# This dev container is intentionally not an S2I Builder, because.. it doesn't have to be.

FROM centos:latest

LABEL maintainer="Michael Vorburger <mike@vorburger.ch>"

ARG MAVEN_VERSION=3.6.0

ENV MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz

RUN yum -y update && \
    yum install -y --setopt=skip_missing_names_on_install=False \
            rsync java-11-openjdk-devel && \
    yum clean all -y && \
    mkdir -p /usr/share/maven && \
    curl -fsSL ${MAVEN_URL} | tar -xzC /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV JAVA_HOME /etc/alternatives/java_sdk

RUN adduser -u 9999 -g 0 quarkus
USER 9999
RUN mkdir /home/quarkus/dev/
WORKDIR /home/quarkus/dev/

COPY pom.xml /home/quarkus/dev/
COPY src /home/quarkus/dev/src

RUN cd /home/quarkus/dev/ && \
    # Running Maven once during container creation already pre-loads Maven dependencies
    # into ~/.m2, which is very useful to start faster when this dev container will start IRL.
    mvn clean compile dependency:go-offline dependency:resolve-plugins && \
    # Due to https://issues.apache.org/jira/browse/MDEP-82, the above does not actually grab
    # all of quarkus-maven-plugin dependencies, so we now also run "mvn quarkus:dev"
    # but have to kill it, once it's up...
    mvn quarkus:dev & \
    until $(curl --output /dev/null --silent --head --fail http://localhost:8080); do printf '.'; sleep 1; done && \
    kill %1

# https://docs.okd.io/latest/creating_images/guidelines.html#openshift-specific-guidelines
COPY mvn-run-dev.sh /home/quarkus/
USER root
RUN mv /home/quarkus/mvn-run-dev.sh /usr/local/bin
RUN chgrp -R 0 /home/quarkus && \
    chmod -R g=u /home/quarkus && \
    # This is not for OpenShift, but so that "docker run -v ...:z -u ..." works:
    chmod -R a+rwx /home/quarkus
USER 9999

CMD ["bash", "-c", "/usr/local/bin/mvn-run-dev.sh"]

EXPOSE 8080
