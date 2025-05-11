ARG JAVA_VERSION=21
ARG GRADLE_IMAGE=gradle:jdk${JAVA_VERSION}-alpine
ARG BASE_IMAGE=ghcr.io/dimantchick/spring-base-image:$JAVA_VERSION

# Build project
FROM ${GRADLE_IMAGE} AS build
WORKDIR /workspace
COPY --chown=gradle:gradle ./ ./

RUN gradle build

# Extract jar
FROM $BASE_IMAGE AS extract
ARG APP_NAME=blackList-0.0.1.jar

COPY --from=build /workspace/build/libs/${APP_NAME}.jar ./app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher

# Build container image
FROM $BASE_IMAGE
LABEL org.opencontainers.image.source=https://github.com/Dimantchick

# Application layers
## Spring|Libraries
COPY --from=extract /workspace/app/dependencies/ ./
COPY --from=extract /workspace/app/spring-boot-loader/ ./
COPY --from=extract /workspace/app/snapshot-dependencies/ ./
## Application
COPY --from=extract /workspace/app/application/ ./
