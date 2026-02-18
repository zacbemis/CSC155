@echo off
setlocal

cd /d %~dp0

javac -cp "." a1\*.java utils\Utils.java
