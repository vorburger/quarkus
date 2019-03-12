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

RUN adduser -u 1001 quarkus-dev
WORKDIR /home/quarkus-dev/
USER 1001

COPY pom.xml /home/quarkus-dev/
COPY src /home/quarkus-dev/src
RUN mvn compile
# ^^^ running Maven once during container creation already pre-loads Maven dependencies into ~/.m2, which is very useful to start faster after
# TODO we should also run "mvn quarkus:dev", but kill it, once it's up...

CMD ["mvn", "compile", "quarkus:dev"]

EXPOSE 8080

# TODO Come up with some black magic whichcraft which restarts 'mvn compile quarkus:dev' whenever the pom.xml changes...

