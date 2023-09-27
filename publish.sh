#!/bin/sh
modelName="modelsqlite"

./gradlew :$modelName:clean --info
./gradlew :$modelName:build --info
./gradlew :$modelName:publish --info