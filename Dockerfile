# perlu graalvm native-image untuk ngerun -Pnative native:compile (yg ada gu installnya)
#FROM ghcr.io/graalvm/native-image:ol8-java17-22 AS builder

# kalo pake base maven kan gada graalvm engine nya
#FROM maven:3.8.3-openjdk-17-slim as builder

#WORKDIR /build
#
#COPY src src/
#COPY pom.xml pom.xml
#
#RUN mvn -Pnative native:compile
#
#FROM docker.io/oraclelinux:8-slim
#
#WORKDIR /app
#
#COPY --from=builder /build/shinigami-api .
#
#ENTRYPOINT ["/shinigami-api"]


## compile to jar in container (multi-stage build) -----------------
#FROM maven:3.8.3-openjdk-17-slim as builder
#
#WORKDIR /build
#
#COPY src src/
#COPY pom.xml pom.xml
#
#RUN mvn clean package
#
##FROM docker.io/oraclelinux:8-slim
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY --from=builder /build/target/*.jar app.jar
#
#CMD ["java", "-jar", "app.jar"]
## ----------------------------------------------


FROM openjdk:17-jdk-slim

WORKDIR "/app"

COPY target/shinigami-api-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Xmx5500M", "-jar", "shinigami-api-0.0.1-SNAPSHOT.jar"]

# docker run --name shinigami-api1 --network shinigami1 -p 8081:8080 -d wiryaimd/shinigami-api:1.0 --spring.datasource.url=jdbc:mysql://5650c48b8952:3306/db_shinigami_8ce117 --spring.datasource.password=8ce117da