# Stage 1 - Build Angular App
FROM node:22 AS ngbuild

WORKDIR /clientsrc

# Install Angular CLI
RUN npm i -g @angular/cli@19.2.1

# Copy Angular project files
COPY ./client/angular.json . 
COPY ./client/package.json . 
COPY ./client/tsconfig.json . 
COPY ./client/tsconfig.app.json . 
COPY ./client/src src

# Install dependencies and build
RUN npm install
RUN ng build

# Stage 2 - Build Spring Boot App
FROM maven:3.9.9-eclipse-temurin-23 AS javabuild

WORKDIR /app

# Copy Spring Boot project files
COPY ./server/pom.xml .
COPY ./server/.mvn .mvn
COPY ./server/mvnw .
COPY ./server/src src

# Grant executable permissions for mvnw
RUN chmod +x mvnw

# Copy data files to resources
COPY ./data/menus.json src/main/resources/
COPY ./data/restaurant.sql src/main/resources/

# Copy Angular build output to Spring Boot static resources
COPY --from=ngbuild /clientsrc/dist/client/browser src/main/resources/static

# Build the Spring Boot application
RUN ./mvnw package -DskipTests

# Stage 3 - Run the Application
FROM openjdk:23

WORKDIR /app

# Copy built JAR
COPY --from=javabuild /app/target/server-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=3000
EXPOSE ${PORT}

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]


# run
# docker build -t cihansifan/anyname:v0.0.1 .

# container
# docker run -p 8085:8080 cihansifan/anyname:v0.0.1