#!/bin/bash
$HOME/go/bin/bombardier -c 50 -n 1000000 http://localhost:8030/500
