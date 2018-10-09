#!/bin/bash

mvn compile assembly:single

java -cp ./target/jrocks-core-0.0.1-jar-with-dependencies.jar picocli.AutoComplete jrocks.shell.JRocksCommand -f

. jrocks_completion

alias jrocks='java -jar target/jrocks-core-0.0.1-jar-with-dependencies.jar'

