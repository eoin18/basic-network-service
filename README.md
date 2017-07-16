Basic Network Service
=====================

A basic network service which can run on two nodes. 

- One node serves requests from a client and does some sanitation on the requests, before serving them to the functional service
- The functional service handles the requests from the client facing service and performs the business logic (In this simple case - an arithmetic evaluation)


###How to run
```
./gradlew clean shadowJar
```
This will build the required shaded JARs for running the application

```
cd client-service
java -jar build/libs/client-service-1.0-SNAPSHOT-all.jar server client-service.yml 
```
Launches the client facing service on port 8080, configured by default to look for a functional service at http://localhost:8082/

```
cd ../function-service
java -jar build/libs/function-service-1.0-SNAPSHOT-all.jar server function-service.yml
```
Launches the function service by default at port 8082

Then using the HTTP Client of your choice make a request to such as:
```
http://localhost:8080/multiply?left=123&right=2
```
