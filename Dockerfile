FROM openjdk:11
ADD build/libs/sneakerboss-0.0.1.jar .
CMD java -jar sneakerboss-0.0.1.jar
EXPOSE 8080
