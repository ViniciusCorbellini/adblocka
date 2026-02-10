# Makefile so i dont need to remember mvn commands

# Variables
JAR_FILE=target/*.jar

# Compiles the project and generates the .jar
build:
	mvn clean package

# Cleans target/
clean:
	mvn clean

# Runs the jar as specified in maven.apache.org guides
run:
	java -cp $(JAR_FILE) com.manocorbax.adblocka.App

up:
	make build
	make run
