From openjdk:8-jdk-alpine
COPY . /app
WORKDIR /app
ADD target/recom-filter-1.0-SNAPSHOT.jar filter.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","filter.jar","--spring.profiles.active=dev"]
