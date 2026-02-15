#!/usr/bin/env bash
set -e

# From a1/, classpath needs parent (..) so a1.Code is found, plus JARs
CP="..:../javagaming/jogl/jogamp-fat.jar:../javagaming/joml/joml-1.10.7.jar"

cd "$(dirname "$0")"
java -cp "$CP" \
  --add-exports java.base/java.lang=ALL-UNNAMED \
  --add-exports java.desktop/sun.awt=ALL-UNNAMED \
  --add-exports java.desktop/sun.java2d=ALL-UNNAMED \
  -Dsun.java2d.d3d=false \
  -Dsun.java2d.uiScale=1 \
  a1.Code
