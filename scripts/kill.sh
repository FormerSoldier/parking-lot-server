#!/bin/bash
echo "stop SpringBoot app"
pid=`ps -ef | grep parking-lot-0.0.1-SNAPSHOT | grep -v grep | awk '{print $2}'`
echo "old app pid：$pid"
if [ -n "$pid" ]
then
kill -9 $pid
fi