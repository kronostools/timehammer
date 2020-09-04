#!/bin/bash

docker image pull qopuir/timehammer-web:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-scheduler:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-core:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-catalog:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-integration:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-comunytek:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-commandprocessor:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-statemachine:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-telegramchatbot:1.0.0-SNAPSHOT
docker image pull qopuir/timehammer-telegramchatbotnotifier:1.0.0-SNAPSHOT