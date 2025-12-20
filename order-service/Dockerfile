# Stage 1: Gradle cache builder (только dependencies)
FROM gradle:8.14-jdk17 AS dependency-resolver

WORKDIR /app

COPY settings.gradle build.gradle ./
COPY eureka-server/build.gradle eureka-server/
COPY config-server/build.gradle config-server/
COPY api-gateway/build.gradle api-gateway/

COPY user-service/build.gradle user-service/
COPY product-service/build.gradle product-service/
COPY order-service/build.gradle order-service/
COPY common/build.gradle common/
# COPY product-service/build.gradle.kts product-service/
# COPY order-service/build.gradle.kts order-service/

RUN gradle :order-service:dependencies --no-daemon 2>&1 | grep -E "^|---|.*" || true

# Stage 2: Builder (только config-server)
FROM dependency-resolver AS order-builder

COPY order-service/src order-service/src
COPY common/src common/src

RUN gradle :order-service:build -x test --no-daemon -q

# Stage 3: Runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=order-builder /app/order-service/build/libs/*.jar app.jar

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8083

HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
    CMD wget --quiet --tries=1 --spider http://localhost:8083/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
