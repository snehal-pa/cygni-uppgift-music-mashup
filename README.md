# cygni-uppgift-music-mashup

### 1. Environment

Following environment is used to build, run and test this project:
- Java 15.0.2
- Spring Boot 2.4.2
- Windows 10 x64
- IntelliJ IDEA 2020.3.2
    
### 2. How to build jar file 

I have followed these steps to build a jar file
- Add `<packaging>jar</packaging>` in `pom.xml`, under `<version>0.0.1-SNAPSHOT</version>`.
- Open the Maven window in IntelliJ on the right, then press the Execute Mavel Goal button (a red V ).
- Run `mvn clean package`. This will build the jar file in `target` folder.
- Move this jar file to a new distribution folder if necessary.



### 3. How to run the project
- Use this command to run this file:
  `java -jar <path_to_the_jar_file>\music_mashup-0.0.1-SNAPSHOT.jar`

### 4. How to test

Some examples to test the API can be run in Postman or browser: 
  - http://localhost:8080/rest/artist/410c9baf-5469-44f6-9852-826524b80c61
  - http://localhost:8080/rest/artist/ed3f4831-e3e0-4dc0-9381-f5649e9df221
  - http://localhost:8080/rest/artist/0ab49580-c84f-44d4-875f-d83760ea2cfe
