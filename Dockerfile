# ====== STAGE 1: build (Maven + Java 17) ======
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copia pom(s) primeiro pra aproveitar cache
COPY pom.xml .
COPY ms-user/pom.xml ms-user/pom.xml
COPY ms-transaction/pom.xml ms-transaction/pom.xml
COPY worker/pom.xml worker/pom.xml
COPY finance-events/pom.xml finance-events/pom.xml

# Baixa dependências (cache)
RUN mvn -q -DskipTests dependency:go-offline

# Copia o resto do código
COPY . .

# Build do multi-módulo
RUN mvn -q -DskipTests clean package

# ====== STAGE 2: runtime (JRE 17 leve) ======
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia os jars gerados
COPY --from=build /build/ms-user/target/*.jar /app/ms-user.jar
COPY --from=build /build/ms-transaction/target/*.jar /app/ms-transaction.jar
COPY --from=build /build/worker/target/*.jar /app/worker.jar

# Boa prática p/ Spring em container
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Por padrão não define CMD fixo.
# Cada Task Definition do ECS vai definir o "command" (qual jar rodar).
ENTRYPOINT ["sh", "-c"]
CMD ["java $JAVA_OPTS -jar /app/ms-user.jar"]