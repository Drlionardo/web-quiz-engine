# Web Quiz Engine
Реализация учебного проекта [Web Quiz Engine](https://hyperskill.org/projects/91?track=1).
Приложение представляет из себя HTTP-сервис обрабатывающий REST запросы. Он позваляет пользователям
создавать и отвечать на тесты с выбором правильного ответа.

## API
#### `POST(api/register)` Регистрация пользователя
```
{
  "email": "test@gmail.com",
  "password": "secret"
}
```

Остальные методы доступны только авторизованным пользователем, используется **HTTP Basic Auth** .
Пароли пользователей хешируются при помощи BCrypt и не хранятся в открытом виде.
#### `POST(api/quizzes)`  Создание теста
```
{
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"],
  "answer": [0,2]
}
```
#### `GET(/api/quizzes/{id})`  Просмотр теста  по его `id`
Пример ответа сервера 
```
{
  "id": 1,
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
}
```
#### `GET(/api/quizzes)` Просмотр всех доступных тестов с поддержкой пагинации
Пример запроса и упрощенного ответа
` GET(/api/quizzes?page=0) `
```
{
  "totalPages":1,
  "totalElements":3,
  "last":true,
  "first":true,
  "sort":{ },
  "number":0,
  "numberOfElements":3,
  "size":10,
  "empty":false,
  "pageable": { },
  "content":[
    {"id":102,"title":"Test 1","text":"Text 1","options":["a","b","c"]},
    {"id":103,"title":"Test 2","text":"Text 2","options":["a", "b", "c", "d"]},
    {"id":202,"title":"The Java Logo","text":"What is depicted on the Java logo?",
     "options":["Robot","Tea leaf","Cup of coffee","Bug"]}
  ]
}
```
На одной странице выводится до 10 тестов.

#### `GET(/api/quizzes/completed)` Просмотр всех успешно решенных тестов с поддержкой пагинации 
```
{
  "totalPages":1,
  "totalElements":5,
  "last":true,
  "first":true,
  "empty":false,
  "content":[
    {"id":103,"completedAt":"2019-10-29T21:13:53.779542"},
    {"id":102,"completedAt":"2019-10-29T21:13:52.324993"},
    {"id":101,"completedAt":"2019-10-29T18:59:58.387267"},
    {"id":101,"completedAt":"2019-10-29T18:59:55.303268"},
    {"id":202,"completedAt":"2019-10-29T18:59:54.033801"}
  ]
}
```
На одной странице выводится до 10 тестов начиная с последних решенных. Может содержать
одинаковые id теста, поскольку тест может быть решен несколько раз в разное время.
#### `Post(api/quizzes{id}/solve)` Решение теста по `ID` 
```
{"answer": [0,2]}
```
Массив `answer` может быть пустым, так так тест может не содержать ни одного правильного ответа.
Ответ сервера:
* В случае успешного ответа 
``` {"success":true,"feedback":"Congratulations, you're right!"} ```
* В случае неверного ответа
``` {"success":false,"feedback":"Wrong answer! Please, try again."} ```
#### `DELETE(/api/quizzes/{id})` Удаление созданных тестов
Пользователь имеет возможность удалять лишь собственные тесты.
В противном случае сервер возвращает `HTTP 403 (Forbidden)` , а также `HTTP 404 (Not found)` в случае отстутсвия теста
## Дополнительно использованные технологии
* Spring Security для ограниченияд доступа неавторизованным пользователям и авторизации через  **HTTP Basic Auth**
* Bean validation для валидации данных в JSON при регистрации, создание тестов.
* Хранение данных в H2 базе данных. Schema базы данных создавалась автоматически при помощи Hibernate.

**Тесты к проекту предоставлены платформой Hyperskill**
