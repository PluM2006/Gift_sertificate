**pageable**

*size* default = 10

*page* default = 1

### CERTIFICATE

***POST***

* ***add certificate***

Request

    http://localhost:8080/api/v1/certificates/

<details>
<summary>body</summary>

```json
{
  "name": "certificate",
  "description": "description",
  "duration": 10,
  "price": 10,
  "tags": [
    {
      "name": "tag"
    }
  ]
}
```

OR

```json
{
  "name": "certificate",
  "description": "description",
  "duration": 10,
  "price": 10
}
```

OR

```json
{
  "name": "certificate",
  "description": "description",
  "duration": 10,
  "price": 10,
  "tags": [
    {
      "name": "tag2"
    },
    {
      "id": 1,
      "name": "tag1"
    }
  ]
}
```

</details>

***UPDATE***

* ***update certificate***

Request

    http://localhost:8080/api/v1/certificates/{id}

***DELETE***

* ***delete certificate***

Request

    http://localhost:8080/api/v1/certificates/{id}

***GET***

* ***find certificate by ID***

Request

    http://localhost:8080/api/v1/certificates/id/{id}

* ***find certificate by NAME***

Request

    http://localhost:8080/api/v1/certificates/name/{name}

* ***find certificate by name tag or description certificate and sorting by different fields***

*tagName* and *description* ignoreCase

Request

    http://localhost:8080/api/v1/certificates?page=0&size=10&description=description&tagName=name&sort=name,asc&sort=description,desc

* ***find certificate by names tags***

*tagName* ignoreCase

Request

    http://localhost:8080/api/v1/certificates/tags/name1,name2,...,name3

### TAG

***POST***

* ***add tag***

Request

    http://localhost:8080/api/v1/tags/

<details>
<summary>body</summary>

```json
{
  "name": "tag_name"
}
```

</details>

***UPDATE***

* ***update tag***

Request

    http://localhost:8080/api/v1/tags/{id}

***DELETE***

* ***delete tag***

Request

    http://localhost:8080/api/v1/tags/{id}

***GET***

* ***find tag by ID***

Request

    http://localhost:8080/api/v1/tags/id/{id}

* ***find popular tags user***

Request

    http://localhost:8080/api/v1/popularTag/{username}

* ***find all tags***

Request

    http://localhost:8080/api/v1/tags?page=0&size=10

### ORDER

***POST***

* ***add order***

Request

    http://localhost:8080/api/v1/orders/

<details>
<summary>body</summary>

```json
{
  "certificate": {
    "id": 1,
    "name": "name"
  },
  "user": {
    "username": "username"
  }
}
```

</details>

***GET***

* ***find all orders user***

Request

    http://localhost:8080/api/v1/orders

<details>
<summary>body</summary>

```json
{
  "id": 1,
  "first_name": "first_name",
  "second_name": "second_name",
  "username": "username"
}
```

</details>

* ***find order by id***

Request

    http://localhost:8080/api/v1/orders/{id}

### USER

***GET***

* ***find all users***

Request

    http://localhost:8080/api/v1/users

* ***find user by id***

Request

    http://localhost:8080/api/v1/users/{id}
