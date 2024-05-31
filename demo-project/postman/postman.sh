#!/bin/bash -eux
env_file=$1
newman run students.postman_collection.json  -e $env_file --reporters cli,json --reporter-json-export ./report/result-1.json
