#!/bin/bash

MVN="mvn.bat  -Dmaven.test.skip=true"

$MVN -f pom-parent.xml install &&
rm -f target/dependency/* &&
$MVN clean package
