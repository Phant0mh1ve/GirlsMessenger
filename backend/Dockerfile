# syntax=docker/dockerfile:1.4
From maven:3.9-eclipse-temurin-23 AS build
WORKDIR /backend
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B dependency:go-offline
COPY src ./src
