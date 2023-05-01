#!/bin/bash
echo BUILDING....

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
cd "$SCRIPT_DIR" || (echo "Not found" && exit)
cd ..
./gradlew build shadowJar || exit
cd docker || (echo "Not found" && exit)
docker compose kill
docker compose down
docker compose up -d