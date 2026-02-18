#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

CP=".:javagaming/jogl/jogamp-fat.jar:javagaming/joml/joml-1.10.7.jar"

javac -cp "$CP" a1/*.java utils/Utils.java
