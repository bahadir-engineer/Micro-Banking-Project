FROM amazoncorretto:21

WORKDIR /backend

COPY build/libs/MicroBankingProject-0.0.1-SNAPSHOT.jar /backend.jar

ENTRYPOINT ["java", "-jar", "/backend.jar"]