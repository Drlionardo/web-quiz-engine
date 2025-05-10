# Web Quiz Engine

An implementation of the educational project [Web Quiz Engine](https://hyperskill.org/projects/91?track=1).  
The application is an HTTP service that handles REST requests. It allows users to create and solve quizzes with multiple-choice answers.

## API

#### `POST(api/register)` User Registration
```json
{
  "email": "test@gmail.com",
  "password": "secret"
}
```

All other methods are only available to authenticated users, using **HTTP Basic Auth**.  
User passwords are hashed using BCrypt and are not stored in plain text.

#### `POST(api/quizzes)` Create a Quiz
```json
{
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano", "Tea", "Cappuccino", "Sprite"],
  "answer": [0, 2]
}
```

#### `GET(/api/quizzes/{id})` View a quiz by `id`  
Example server response:
```json
{
  "id": 1,
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"]
}
```

#### `GET(/api/quizzes)` View all available quizzes with pagination  
Example request and simplified response:  
`GET(/api/quizzes?page=0)`
```json
{
  "totalPages": 1,
  "totalElements": 3,
  "last": true,
  "first": true,
  "sort": {},
  "number": 0,
  "numberOfElements": 3,
  "size": 10,
  "empty": false,
  "pageable": {},
  "content": [
    {"id": 102, "title": "Test 1", "text": "Text 1", "options": ["a", "b", "c"]},
    {"id": 103, "title": "Test 2", "text": "Text 2", "options": ["a", "b", "c", "d"]},
    {"id": 202, "title": "The Java Logo", "text": "What is depicted on the Java logo?",
     "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"]}
  ]
}
```
Each page displays up to 10 quizzes.

#### `GET(/api/quizzes/completed)` View all successfully solved quizzes with pagination  
```json
{
  "totalPages": 1,
  "totalElements": 5,
  "last": true,
  "first": true,
  "empty": false,
  "content": [
    {"id": 103, "completedAt": "2019-10-29T21:13:53.779542"},
    {"id": 102, "completedAt": "2019-10-29T21:13:52.324993"},
    {"id": 101, "completedAt": "2019-10-29T18:59:58.387267"},
    {"id": 101, "completedAt": "2019-10-29T18:59:55.303268"},
    {"id": 202, "completedAt": "2019-10-29T18:59:54.033801"}
  ]
}
```
Each page shows up to 10 results, starting with the most recently completed.  
The same quiz `id` can appear multiple times, as a quiz may be solved multiple times at different moments.

#### `POST(api/quizzes/{id}/solve)` Solve a quiz by `ID`
```json
{"answer": [0, 2]}
```
The `answer` array may be empty if the quiz has no correct answers.  
Server response:
* If successful:  
```json
{"success": true, "feedback": "Congratulations, you're right!"}
```
* If incorrect:  
```json
{"success": false, "feedback": "Wrong answer! Please, try again."}
```

#### `DELETE(/api/quizzes/{id})` Delete a quiz  
Users can delete only their own quizzes.  
Otherwise, the server returns `HTTP 403 (Forbidden)`, or `HTTP 404 (Not Found)` if the quiz doesn't exist.

## Additional Technologies Used
* Spring Security to restrict access for unauthorized users and authorize via **HTTP Basic Auth**.
* Bean Validation for validating JSON data during registration and quiz creation.
* Data stored in an H2 database. The schema was auto-generated using Hibernate.

**Tests for the project are provided by the Hyperskill platform.**
