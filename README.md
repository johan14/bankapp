# Bank app

This is an mock bank application which provides simple functionalities such as account creation, transfers etc.

## Table of Contents

- [Intro](#intro)
- [Installation](#installation)
- [Usage](#usage)
- [Structure](#structure)
- [API Documentation](#api-documentation)
- [Testing Coverage](#testing-coverage)

## Intro

This is a Spring Boot application which serves HTTP requests in order to provide simple banking functionalities.

## Installation

Running these commands:

`mvn clean install`

`java -jar target/bankapp-0.0.1-SNAPSHOT.jar`

## Usage

After running the application, you can hit `localhost:8080/api/v1/addCustomer` endpoint with a POST request to create a customer, which body is as below:

```
{
    "id":2,
    "name":"James Doe"
}
```

Then you can hit `localhost:8080/api/v1/addAccount` endpoint to create an account with this body:

```
{
    "accountNo":2,
    "balance":100,
    "customerId":2
}
```
## Structure

Sequence diagram:

![bank](https://github.com/johan14/bankapp/assets/28931298/ef43a245-c09d-4c43-b2d9-5f3f99ff584d)

## API Documentation

This application uses **Swagger** for API documentation.

![image](https://github.com/johan14/bankapp/assets/28931298/cbb7c99a-b737-40ba-bff8-b675e968751e)

## Testing Coverage

There is a 91% testing coverage using **JUnit** library for integration test.

![image](https://github.com/johan14/bankapp/assets/28931298/6863d0a6-013d-4e23-bb83-4ed67d6adfbf)



