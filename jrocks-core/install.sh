#!/bin/bash

cd `dirname $0`

mvn package spring-boot:repackage
alias jrocks="java -jar `pwd`/target/jrocks*.jar"

cd -
