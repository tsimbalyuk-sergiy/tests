#!/bin/bash
./http-client/target/http-client -Dmicronaut.server.port=8030 -XX:+PrintGCSummary
#-XX:StartFlightRecording:filename=recording-http-client.jfr