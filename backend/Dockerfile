# First stage - BUILD

# this image already has linux and jdk inside
FROM eclipse-temurin:25-jdk AS build
# set the current directory and creates it if needed
WORKDIR /app

COPY . .

RUN ./gradlew bootJar

# Second Stage - RUN

# Smaller image just with the runtime
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080

LABEL authors="oleksandr.vafin"
ENTRYPOINT ["java", "-jar", "app.jar"]