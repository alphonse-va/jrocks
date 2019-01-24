#!/bin/bash

SCRIPT_PATH=~/bin/jrocks

cd `dirname $0`

if [ ! -d ~/bin ]; then
  mkdir ~/bin
fi

echo "java -Dloader.path='`pwd`/plugins:`pwd`/templates:`pwd`/config'
           -Drocker.reloading=true
           -jar `pwd`/jrocks-core-${project.version}-run.jar" > ${SCRIPT_PATH}

chmod a+x ${SCRIPT_PATH}

echo
echo "JRocks installed with success."
echo

# cd without echo directory
cd ~-
