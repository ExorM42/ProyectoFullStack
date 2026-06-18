FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
COPY src/main/resources/wallet /app/wallet
EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]
