#!/bin/bash

function usage() {
  echo "Usage: $0 <start|stop|check|test|configure|kill>"
}

case $1 in
  "configure")
    java -jar start.jar --add-to-start=http,websocket,deploy,console-capture,requestlog
  ;;

  "start")
    nohup java -Duser.timezone=UTC -Dfile.encoding=UTF8 -Dname=cometdjetty -jar start.jar STOP.PORT=28282 STOP.KEY=secret & 1>/dev/null 2>&1 &
  ;;

  "stop")
    java -jar start.jar STOP.PORT=28282 STOP.KEY=secret --stop
  ;;

  "check")
    pid=`ps aux | grep 'name=[c]ometdjetty' | awk '{print $2}'`
    echo "CometDJetty is running as pid: $pid"
  ;;

  "kill")
    pid=`ps aux | grep 'name=[c]ometdjetty' | awk '{print $2}'`
    if [ ! -z "$pid" ]; then
      echo "Killing CometDJetty with pid: $pid";
      kill -9 "$pid"
    else
      echo "CometDJetty not running";
    fi
  ;;

  "test")
    echo "Not implemented";

  ;;

  *)
    echo -n "Unknown command:";
    usage;
    exit 1;
  ;;
esac
