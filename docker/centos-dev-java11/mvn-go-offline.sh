#!/bin/bash
set -ex

# Running Maven once during container creation already pre-loads Maven dependencies
# into ~/.m2, which is very useful to start faster when this dev container will start IRL.
mvn clean compile dependency:go-offline dependency:resolve-plugins

# Due to https://issues.apache.org/jira/browse/MDEP-82, the above does not actually grab
# all of quarkus-maven-plugin dependencies, so we now also run "mvn quarkus:dev"
# but have to kill it, once it's up...
mvn quarkus:dev &
until $(curl --output /dev/null --silent --head --fail http://localhost:8080); do printf '.'; sleep 1; done
kill %1
