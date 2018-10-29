#!/usr/bin/env bash

PACKAGE_DIR=`pwd`

cd ../jrocks-parent

mvn clean package

cd ~

unzip -o $PACKAGE_DIR/target/jrocks-*-bin.zip

jrocks/install.sh

cd $PACKAGE_DIR
