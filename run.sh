#!/bin/bash

SOURCE_DIR="/home/angeli/Asa/Sprint(Mr Naina)/spring-framework-by-angeli"
TARGET_JAR="$SOURCE_DIR/target/spring-framework-by-angeli-1.0-SNAPSHOT-jar-with-dependencies.jar"

TEST_DIR="/home/angeli/Asa/Sprint(Mr Naina)/test"
TEST_WAR="$TEST_DIR/target/test-test.war"

TOMCAT_PATH="/home/angeli/Asa/Sprint(Mr Naina)/Tomcat/"

cd "$SOURCE_DIR"

mvn clean package


cp "$TARGET_JAR" "$TEST_DIR/src/main/webapp/WEB-INF/lib"

cd "$TEST_DIR"
mvn clean package


cp "$TEST_WAR" "$TOMCAT_PATH/webapps"
"$TOMCAT_PATH/bin/startup.sh"