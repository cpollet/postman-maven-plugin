#!/bin/sh

ls -l /data

max=$1
host=$2
collection=$3

counter=0
while [ "`curl --connect-timeout 1 http://$host:8080/ready 2&>/dev/null | grep OK`" != "OK" ]; do
    counter=$((counter+1))

    if [ $counter -eq $((max+1)) ]; then
        echo http://$host:8080 did not start
        echo 1 > /data/$collection.result
        exit 1
    fi

    echo "Waiting for http://$host:8080 ($counter / $max) ..."
    sleep 1
done

SEARCH='"request" : {'
REPLACE='"event":[{"listen":"test","script":{"type":"text/javascript","exec":["pm.test('\''Body matches string'\'',function(){","pm.expect(pm.response.text()).to.include('\''OK'\'');","});"]}}],\n    "request":{'
sed -e "s|$SEARCH|$REPLACE|g" /data/$collection.json > /data/$collection-assert.json

newman run /data/$collection-assert.json # 2&>1 > /data/$collection.newman

echo -n $? > /data/$collection.result
