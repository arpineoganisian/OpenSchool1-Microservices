# OpenSchool1-Microservices
Microservices using Spring JPA, Spring Boot and RestTemplate

## How to run the application
`docker-compose up -d`

## How to test the application
`curl -X GET 'http://localhost:8081/consumer/products'`  
`curl -X GET 'http://localhost:8081/consumer/products?page=0&size=2&min_price=15&max_price=20'` 

