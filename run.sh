#!/bin/bash
javac *.java ./main/*.java ./structures/*.java -d ./bin/ -cp ./
cd bin
java Game
