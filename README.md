# Facility Reservation

## Description

Office Facility(Phone) Reservation System

## Requirements

* Java 17 (17.0.1.12.1-amzn).
* Maven 3.9.5

## Documentation
 Postman collection
> ./Facility_Reservation_System.postman_collection.json
 
 UML diagram
> ./Facility_Reservation_System.drawio.pdf

## Phone Booking Services 
>[Open API Documentation](http://localhost:8080/swagger-ui/index.html#/Phone%20Booking%20Services)

#### 1. List available phones
Endpoint:

```
GET /phones
```

Response:

```json
[
  {
    "bookedBy": "Tester",
    "bookedSince": "2024-03-15T10:05:48.194056",
    "available": false,
    "updatedOn": "2024-03-15T10:05:48.20328",
    "id": 1,
    "brand": "Samsung",
    "model": "Galaxy S9"
  },
  {
    "available": true,
    "updatedOn": "2024-03-15T10:05:05.906029",
    "id": 2,
    "brand": "Samsung",
    "model": "Galaxy S8"
  }
]
```

#### 2. Get Phone By Id

Endpoint:

```
GET /phones/:id
```

Response:

```json
{
  "available": true,
  "updatedOn": "2024-03-15T10:05:05.903511",
  "id": 1,
  "brand": "Samsung",
  "model": "Galaxy S9"
}
```

If Phone Booked:

```json
{
  "bookedBy": "Tester",
  "bookedSince": "2024-03-15T10:05:48.194056",
  "available": false,
  "updatedOn": "2024-03-15T10:05:48.20328",
  "id": 1,
  "brand": "Samsung",
  "model": "Galaxy S9"
}
```

#### 3. Book the phone

Endpoint:

```
POST /phones/:id/book
```

Request:

```json
{
  "username": "Tester"
}
```

Response:

```
Status code: 200(ok)
```

#### 4. Return the phone

Endpoint:

```
POST /phones/:id/return
```

Response:

```
Status code: 200(ok)
```

#### 5. Show the phone changes history

Endpoint:

```
Get /phones/:id/history
```

Response:

```json
[
  {
    "phone": {
      "bookedBy": "Tester",
      "bookedSince": "2024-03-15T10:05:48.194056",
      "available": false,
      "updatedOn": "2024-03-15T10:05:48.20328",
      "id": 1,
      "brand": "Samsung",
      "model": "Galaxy S9"
    },
    "revisionNumber": 1,
    "revisionType": "UPDATE"
  },
  {
    "phone": {
      "available": true,
      "updatedOn": "2024-03-15T10:13:36.35091",
      "id": 1,
      "brand": "Samsung",
      "model": "Galaxy S9"
    },
    "revisionNumber": 2,
    "revisionType": "UPDATE"
  }
]
```

## Phone Management Services 
> [Open API Documentation](http://localhost:8080/swagger-ui/index.html#/Phone%20Services)

#### Authorization:
```
Basic auth:
username: admin
password: password

cURL --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ='
```

#### 1. Create a phone
Endpoint:

```
POST /private/phone
```

Request:

```json
{
  "brand": "Samsung",
  "model": "A50"
}
```
Response:

```
Status code: 200(ok)
```

#### 2. Update the phone

Endpoint:

```
PUT /private/phone/:id
```

Request:

```json
{
  "brand": "Samsung",
  "model": "A70"
}
```
Response:

```
Status code: 200(ok)
```

#### 3. Delete the phone

Endpoint:

```
DELETE /private/phone/:id
```

Response:

```
Status code: 200(ok)
```


## Database

#### H2 Database
[Local Url](http://localhost:8080/h2-console/)
```
URL: jdbc:h2:mem:testdb
username: sa
password: password
```

## Maven

#### Build

```shell
mvn clean install
```

#### Execute Unit Tests

```shell
mvn clean verify
```

JaCoCo Report: _<project-dir>/target/site/jacoco/index.html_

Minimum Test Coverage: **80%**


## Run Application

Spring Boot Main Class:

```
am.paruyr.tests.facility.reservation.FacilityReservationApplication
```

Executable Jar:

```shell
java -jar ./target/facility-reservation-0.0.1-SNAPSHOT.jar
```

Docker:

```shell
docker build -t facility-reservation .
```

```shell
docker run -d -p 8080:8080 facility-reservation
```
