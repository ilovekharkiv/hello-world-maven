#!/usr/bin/env bash
export IMAGE=$1
docker-compose stop
docker-compose -f docker-compose.yaml up --detach
echo "Success"