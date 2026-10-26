#!/bin/bash

# Genere la javadoc du framework dans doc/javadoc

# Les paths
BASE_DIR=$(cd "$(dirname "$0")" && pwd)
SOURCE_DIR="$BASE_DIR/spring-framework-by-angeli"
OUT_DIR="$BASE_DIR/doc/javadoc"
CP_FILE="$SOURCE_DIR/target/javadoc-classpath.txt"

set -e

cd "$SOURCE_DIR"

# Classpath des dependances (servlet-api, classgraph, jackson) pour que javadoc resolve les imports
mvn -q dependency:build-classpath -Dmdep.outputFile="$CP_FILE"

rm -rf "$OUT_DIR"

# -private : inclut aussi les methodes package-private et privees
javadoc -d "$OUT_DIR" \
    -sourcepath src/main/java \
    -subpackages angeli.sprint \
    -cp "$(cat "$CP_FILE")" \
    -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 \
    -private \
    -windowtitle "spring-framework-by-angeli" \
    -doctitle "spring-framework-by-angeli - Javadoc" \
    -Xdoclint:all,-missing

echo "Javadoc generee : $OUT_DIR/index.html"
