#!/usr/bin/env bash
set -e

# From a1/, javagaming is at ../javagaming
CP=".:../javagaming/jogl/jogamp-fat.jar:../javagaming/joml/joml-1.10.7.jar"

javac -cp "$CP" *.java
