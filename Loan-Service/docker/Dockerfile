FROM airdock/oracle-jdk:1.8
COPY target/Loan-Service-1.0.0.jar docker/LoanService.jar
EXPOSE 8080
CMD ["java","-jar","docker/LoanService.jar"]