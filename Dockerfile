FROM eclipse-temurin:17.0.4_8-jre-jammy

COPY build/libs /kerberos
WORKDIR /kerberos
EXPOSE 8080
CMD ["java", "-jar", "kerberos-service-0.0.1-SNAPSHOT.jar"]