#!/bin/bash
chmod 777 build/libs/parking-lot-0.0.1-SNAPSHOT.jar
mv -f build/libs/parking-lot-0.0.1-SNAPSHOT.jar /approot/parking-lot-server
java -jar /approot/parking-lot-server/parking-lot-0.0.1-SNAPSHOT.jar