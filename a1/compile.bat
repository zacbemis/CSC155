@echo off
setlocal

set CP=.;..\javagaming;..\javagaming\jogl\jogamp-fat.jar;..\javagaming\joml\joml-1.10.7.jar

javac -cp "%CP%" *.java ..\javagaming\utils\Utils.java
