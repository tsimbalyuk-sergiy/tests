#!/bin/bash
mvn -f http-client/pom.xml clean package -Dpackaging=native-image
