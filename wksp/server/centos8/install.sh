#!/bin/bash

dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo

dnf list docker-ce

dnf install docker-ce --nobest -y

systemctl start docker
systemctl enable docker

docker --version

dnf install curl -y

curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose

docker-compose --version

dnf install git vim -y

useradd timehammer
passwd timehammer
usermod -aG wheel timehammer
usermod -aG docker timehammer
mkdir -p /home/timehammer/.m2/repository