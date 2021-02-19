#!/bin/bash
 mvn -f http-client/pom.xml clean package
java -jar http-client/target/http-client-*.jar -Xmx128M -XUseShenandoahGC -XX:+UnlockExperimentalVMOptions
