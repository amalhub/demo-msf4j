FROM airdock/oracle-jdk:1.8
COPY target/Customer-Service-1.0.0.jar docker/CustomerService.jar
EXPOSE 8080
CMD ["java","-jar","docker/CustomerService.jar"]