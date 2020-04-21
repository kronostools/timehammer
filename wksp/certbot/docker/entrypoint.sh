#!/bin/sh

mkdir -p /tmp/dummycerts
openssl req -x509 -nodes -newkey rsa:2048 -days 1825 -keyout /tmp/dummycerts/privkey.pem -out /tmp/dummycerts/fullchain.pem -subj '/CN=localhost'

if [ ! -e "/etc/letsencrypt/live/${TIMEHAMMER_HOST}/privkey.pem" ] || [ ! -e "/etc/letsencrypt/live/${TIMEHAMMER_HOST}/fullchain.pem" ]; then
  mkdir -p "/etc/letsencrypt/live/${TIMEHAMMER_HOST}"
  cp /tmp/dummycerts/privkey.pem "/etc/letsencrypt/live/${TIMEHAMMER_HOST}/privkey.pem"
  cp /tmp/dummycerts/fullchain.pem "/etc/letsencrypt/live/${TIMEHAMMER_HOST}/fullchain.pem"
fi

trap exit TERM; while :; do certbot renew; sleep 12h & wait ${!}; done;