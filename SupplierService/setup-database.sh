#!/bin/bash

set -e

service postgresql start
psql -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"

