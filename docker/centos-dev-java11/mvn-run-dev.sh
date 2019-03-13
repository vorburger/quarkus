#!/bin/bash
set -ex

# TODO Come up with some black magic whichcraft in mvn-run-dev.sh which restarts 'mvn compile quarkus:dev' whenever the pom.xml changes...

mvn compile quarkus:dev -Dquarkus.http.host=0.0.0.0

