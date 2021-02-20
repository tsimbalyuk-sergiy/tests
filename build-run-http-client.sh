#!/bin/bash
mvn -f http-client/pom.xml clean package -DskipTests
java -jar http-client/target/http-client-*.jar -Xmx128M -XX:+UseShenandoahGC
