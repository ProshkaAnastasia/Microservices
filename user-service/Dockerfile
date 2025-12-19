# Stage 1: Gradle cache builder (только dependencies)
FROM gradle:8.14-jdk17 AS dependency-resolver

WORKDIR /app

COPY settings.gradle build.gradle ./
COPY eureka-server/build.gradle eureka-server/
COPY config-server/build.gradle config-server/
COPY api-gateway/build.gradle api-gateway/
COPY common/build.gradle common/
COPY user-service/build.gradle user-service/
COPY product-service/build.gradle product-service/

RUN gradle :user-service:dependencies --no-daemon 2>&1 | grep -E "^|---|.*" || true


# Stage 2: Builder
FROM dependency-resolver AS user-builder

COPY common/src common/src
COPY user-service/src user-service/src

RUN gradle :user-service:build -x test --no-daemon -q


# Stage 3: Runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=user-builder /app/user-service/build/libs/*.jar app.jar

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8081

HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
    CMD wget --quiet --tries=1 --spider http://localhost:8081/api/v1/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
