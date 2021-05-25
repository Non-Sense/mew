#!/usr/bin/env bash
mysql -u root -ppassword < "/docker-entrypoint-initdb.d/init.sql"