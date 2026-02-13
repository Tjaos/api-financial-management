FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# copia o jar já construído no host (garanta que 'mvn package' rodou)
COPY target/*.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
