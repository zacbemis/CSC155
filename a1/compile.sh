#!/usr/bin/env bash
set -e

CP=".:../javagaming:../javagaming/jogl/jogamp-fat.jar:../javagaming/joml/joml-1.10.7.jar"

javac -cp "$CP" *.java ../javagaming/utils/Utils.java
