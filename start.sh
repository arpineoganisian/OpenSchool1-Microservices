#!/bin/bash

docker build -t supplier SupplierService/
docker build -t consumer ConsumerService/
docker run -p 8080:8080 -t supplier
docker run -p 8081:8081 -t consumer
docker-compose up -d
