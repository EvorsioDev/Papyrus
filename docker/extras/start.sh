#!/bin/bash

mkdir -p "/data/plugins/ServerBootstraper"
cp -v /plugin/papyrus-bukkit/build/libs/papyrus-bukkit-1.0.jar /data/plugins/
cp -v /extras/*.jar /data/plugins/
cp -v /extras/bukkit-startup.yml /data/plugins/ServerBootstraper/config.yml
/start