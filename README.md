![coverage](https://gitlab.ewi.tudelft.nl/cse2115/2020-2010/8-corona-proof-room-scheduling/op29-sem57/badges/master/coverage.svg)
![pipeline](https://gitlab.ewi.tudelft.nl/cse2115/2020-2010/8-corona-proof-room-scheduling/op29-sem57/badges/master/pipeline.svg)  
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/026a9b47867c71d02bad)

# CSE2115 - Corona-Proof Room Scheduling Project
## Group OP29-SEM57:  
Alexandru Bobe, Can Parlar, Luca Becheanu, Matthijs de Goede, Merdan Durmuş, Timen Zandbergen

### Running
To run the main server:  
`gradle bootRun`  
To generate the jar files:  
`gradle :[service]:bootJar`  
Which can be found in:   
`subprojects/[service]/libs/[service]-[version].jar`  

### Testing
```
gradle test
```
To generate a coverage report:
```
gradle jacocoTestReport
```
The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open index.html in your browser to see the report.

### Postman

We have created a Postman collection of example requests for all the 
API endpoints. Documentation of all the endpoints can be found under: 
```
/docs/sprint4/2020-12-18-postman-documentation.html
```
This collection can be imported in postman by clicking on the orange button.
We make use of variables, so a session token can be updated in a single place and will be used for all requests. 
To successfully run all requests at once, one should login as both a a teacher and a student and save the tokens in the dedicated variables. 
Under identity service, you can login as both a student and a teacher. 
To proceed, copy the token in the response, click options, edit and under the variables tab paste it for the variable "testTeacherToken" then click persist all and do the same for the student login.  After that you can run each request individually, or all at once.


### Static analysis
```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

### Database Information
Our project has 5 databases all hosted privately on a vps.  
```
hostname:    timenzan.nl
username:    semgroup
password:    5d42d7790150c738b40801bbed1f8524777563a13
databases: calendar, course, identity, restriction, room
```

### Docs
Inside the /doc folder, we have our sprint files. In each sprint we have a retrospective for that week and files of that week.  
The initial requirements are in /doc/sprint0  
Our database schema and domain overview are in /doc/sprint1  
Our final documents and reflections are in /doc/sprint4  
