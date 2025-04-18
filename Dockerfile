# Use lightweight OpenJDK base image
FROM openjdk:17-jdk-slim
VOLUME /tmp

# Copy your built JAR into the image
COPY target/iot-data-streaming-0.0.1-SNAPSHOT.jar iot-data-app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "/iot-data-app.jar"]
