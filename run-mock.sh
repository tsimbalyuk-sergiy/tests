#!/bin/bash
mvn -f mock-rest/pom.xml clean package
java -jar mock-rest/target/mock-rest-*.jar -Xmx128M -XUseShenandoahGC -XX:+UnlockExperimentalVMOptions
