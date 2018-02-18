### Tictactoe server.
#### This is server for games Tictactoe. Implemented for demonstration java-development.
#### Used: Spring Boot, JDBC, MySQL, JUnit.
#### Build with Maven
#### Web-service is RESTful
#### Example of API usage:


* Creating new game:
  * URL   http://localhost:8080/games
  * Method POST
  * Request body 
```
{
  "userName": "user1",
  "size": 3
}
```
  * Success Response:
```
{
  "status": "ok",
  "code": 0,
  "accessToken": "jbcgvetwf21543wgt21",
  "gameToken": "oiu4534h45b4yr4"
}
```
