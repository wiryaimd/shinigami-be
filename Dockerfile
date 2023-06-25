FROM openjdk:17-jdk-slim

WORKDIR "/app"

COPY target/shinigami-api-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Xmx5500M", "-jar", "shinigami-api-0.0.1-SNAPSHOT.jar"]

# docker run --name shinigami-api1 --network shinigami1 -p 8081:8080 -d wiryaimd/shinigami-api:1.0 --spring.datasource.url=jdbc:mysql://5650c48b8952:3306/db_shinigami_8ce117 --spring.datasource.password=