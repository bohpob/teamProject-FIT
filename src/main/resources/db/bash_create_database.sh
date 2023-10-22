#!/bin/bash

sudo -u postgres psql <<EOF
CREATE DATABASE chipin OWNER postgres;
\c chipin;
CREATE SCHEMA chipin;
EOF