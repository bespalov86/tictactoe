## Tictactoe server.
### This is server for games Tictactoe. Implemented for demonstration java-development.
### Used: Spring Boot, JDBC, MySQL, JUnit.
### Build with Maven
### Web-service is RESTful

### Example of API usage:

#### Creating new game:
* URL http://localhost:8080/games
* Method POST
* Request body 
```json
{
  "userName": "user1",
  "size": 3
}
```
* Success Response:
```json
{
  "status": "Ok",
  "code": 0,
  "accessToken": "accessTokenUSER1",
  "gameToken": "gameTokenABC"
}
```
* Error Response:
```json
{
  "status": "error",
  "code": 404,
}
```

#### Get games list:
* URL http://localhost:8080/games
* Method GET
* Request body empty
* Success Response:
```json
{
  "status": "Ok",
  "code": 0,
  "games":
    [
      {
        "gameToken": "gameTokenABC",
        "owner": "user1",
        "opponent": "",
        "size": 3,
        "gameDuration": 12345,
        "state": "creating"
      }
    ]
}
```

#### Join existing game
* URL http://localhost:8080/join
* Method POST
* Request body
```json
{
  "gameToken": "gameTokenABC",
  "userName": "user2"
}
```
* Success Response:
```json
{
  "status": "Ok",
  "code": 0,
  "accessToken": "accessTokenUSER2",
}
```

#### Do step in game
* URL http://localhost:8080/step
* Method POST
* HTTP-header "accessToken": "accessTokenUSER1"
* Request body
```json
{
  "row": 0,
  "col": 0
}
```
* Success Response:
```json
{
  "status": "Ok",
  "code": 0,
}
```

#### Getting game state
* URL http://localhost:8080/state
* Method POST
* HTTP-header "accessToken": "accessTokenUSER1"
* Request body empty
* Success Response:
```json
{
  "status": "Ok",
  "code": 0,
  "youTurn": true,
  "gameDuration": 54321,
  "field":
    [
      "X0?",
      "X?0",
      "X??"
    ]
  "winner": "user1"
}
```
