# 2_3 alinea f

## mvn test

Computes unit tests using surefire

## mvn package

Runs all unit tests and creates JAR/WAR

## mvn package -DskipTests=true

Skips tests and builds JAR/WAR (but still compiles the test classes)

## mvn failsafe:integration-test

Uses failsafe plugin to run tests with names ending in "IT.java" (integration tests).

## mvn install

Runs unit and IT tests and packages into JAR/WAR (basically does everything)
