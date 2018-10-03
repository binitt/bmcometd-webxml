#!/bin/bash

rsync -av target/*.war jetty/webapps/bmcometd.xml linux@linux13:cometd/jetty-cometd/webapps/
rsync -av scripts/jetty.sh linux@linux13:cometd/jetty-cometd/


rsync -av target/*.war jetty/webapps/bmcometd.xml linux@linux12:cometd/jetty-cometd/webapps/
rsync -av scripts/jetty.sh linux@linux12:cometd/jetty-cometd/
