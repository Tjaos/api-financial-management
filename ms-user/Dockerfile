#----------- build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

#----------- runtime ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/ms-user/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
