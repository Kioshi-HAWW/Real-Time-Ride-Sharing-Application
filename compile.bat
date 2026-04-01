@echo off
if not exist "bin" mkdir bin
javac -d bin -cp "lib/sqlite-jdbc.jar;src" src/shared/*.java src/server/*.java src/client/*.java
echo Compilation successful.
pause
