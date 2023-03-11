FROM maven
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests
ENTRYPOINT ["java","-jar","target/AuctionApp-0.0.1-SNAPSHOT.jar"]