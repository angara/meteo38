#!/bin/bash

set -a
source .env

bb run main >> ../log/meteo38.log 2>&1

#.
