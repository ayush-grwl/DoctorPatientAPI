# Java Backend API For Doctor And Patient

### List of Libraries/Frameworks Used

1) spring-boot-starter-web -> For Creating Web Services
2) spring-boot-starter-data-jpa -> For Connecting With Database
3) mysql-connector-java -> Connector For Connecting MySQL DataBase
4) spring-boot-starter-validation -> For Validating The Data At Server Side
5) spring-boot-devtools -> For The Live Server, As Soon As We Do Changes In Our Application The Changes Are Reflected
6) springfox-boot-starter -> For Documentating API on Swagger
7) springfox-swagger-ui -> For Documentating API on Swagger


### Postman Collection URL

1) https://www.postman.com/collections/934b3a33d924117a3121 (It contains all the endpoints which can be viewed in JSON Viewer)


### How to run the springboot application

1) Download the zip folder and open the Application in Spring Boot supported IDE

2) Change the DataBase configuration in Application.properties(src/main/resources) according to database used.
   a) Driver Class Name
   b) URL
   c) Username
   d) Password
   e) Dialect

3) Run the JavaAssignmentRestApiApplication.java(src/main/java/com/rest_api) as a Spring Boot App
