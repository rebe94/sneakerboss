# Use the official Gradle image
FROM gradle:6.8.3-jdk11

# Copy the gradle project files
COPY . /app

# Set the working directory
WORKDIR /app

# Run gradle commands
RUN gradle clean build -x test

# Use openjdk 11
FROM openjdk:11

# Copy the jar file from the previous image
COPY --from=0 /app/build/libs/*.jar sneakerboss-0.0.1.jar

# Run the jar file
CMD ["java", "-jar", "sneakerboss-0.0.1.jar"]
