#!/bin/bash
mvn -f mock-rest/pom.xml clean package -DskipTests
java -jar mock-rest/target/mock-rest-*.jar -Xmx128M -XX:+UseShenandoahGC