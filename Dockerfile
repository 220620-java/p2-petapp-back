FROM maven:3.8.6-openjdk-8
COPY src/ src/
COPY pom.xml pom.xml

RUN mvn clean
RUN mvn test
RUN mvn spring-boot:run