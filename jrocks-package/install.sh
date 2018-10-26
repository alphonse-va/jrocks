#!/bin/bash

ALIAS_FILENAME=~/.jrocks_alias

cd `dirname $0`

mvn package spring-boot:repackage -D skipTests

if [ -f ~/.bashrc ]; then
  if grep -q "jrocks_alias" ~/.bashrc; then
    echo "alias jrocks='java -jar `pwd`/target/jrocks*.jar'" > ${ALIAS_FILENAME}
  else
    echo -e "\n# JRocks\nif [ -f ~/.jrocks_alias ]; then \n. ~/.jrocks_alias\nfi" >> ~/.bashrc
    echo "alias jrocks='java -jar `pwd`/target/jrocks*.jar'" > ${ALIAS_FILENAME}
  fi
fi

if [ -f ~/.zshrc ]; then
  if grep -q "jrocks_alias" ~/.zshrc; then
    echo "alias jrocks='java -jar `pwd`/target/jrocks*.jar'" > ${ALIAS_FILENAME}
  else
    echo -e "\n# JRocks\nif [ -f ~/.jrocks_alias ]; then \n. ~/.jrocks_alias\nfi" >> ~/.zshrc
    echo "alias jrocks='java -jar `pwd`/target/jrocks*.jar'" > ${ALIAS_FILENAME}
  fi
fi

echo
echo "- JRocks installed with success."
echo
echo "Please execute '. ~/.jrocks_alias' to apply changes into your current shell"
echo
# cd without echo directory
cd ~-
