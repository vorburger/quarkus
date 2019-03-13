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
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
ENV JAVA_HOME /etc/alternatives/java_sdk

RUN adduser -u 9999 quarkus-dev
WORKDIR /home/quarkus-dev/
USER 9999

COPY pom.xml /home/quarkus-dev/
COPY src /home/quarkus-dev/src

COPY *.sh /home/quarkus-dev/
RUN cd /home/quarkus-dev/ && \
    ./mvn-go-offline.sh && \
    rm /home/quarkus-dev/mvn-go-offline.sh

# https://docs.okd.io/latest/creating_images/guidelines.html#openshift-specific-guidelines
USER root
RUN mv /home/quarkus-dev/mvn-run-dev.sh /usr/local/bin
RUN chgrp -R 0 /home/quarkus-dev && \
    chmod -R g=u /home/quarkus-dev
USER 9999

CMD ["bash", "-c", "/usr/local/bin/mvn-run-dev.sh"]

EXPOSE 8080

