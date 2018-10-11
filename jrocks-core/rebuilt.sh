#!/bin/bash

mvn package spring-boot:repackage

echo 'alias jrocks="java -jar `pwd`/target/jrocks*.jar"'

