@echo off
setlocal

cd /d %~dp0

set CP=.;javagaming\jogl\jogamp-fat.jar;javagaming\joml\joml-1.10.7.jar

javac -cp "%CP%" a1\*.java javagaming\utils\Utils.java
