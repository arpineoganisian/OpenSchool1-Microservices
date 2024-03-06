# OpenSchool1-Microservices
Microservices using Spring JPA, Spring Boot, RestTemplate and Docker

## How to run the application
`docker-compose up -d`

## How to test the application
`curl -X GET 'http://localhost:8081/consumer/products'`  
  
`curl -X GET 'http://localhost:8081/consumer/products?page=0&size=2'`   

`curl -X GET 'http://localhost:8081/consumer/products?min_price=15&max_price=20'`   
  
`curl -X GET 'http://localhost:8081/consumer/products?category=home'`   

`curl -X GET 'http://localhost:8081/consumer/products/1'`   

`curl -X POST http://localhost:8081/consumer/products -H "Content-Type: application/json" -d '{"name": "Dune", "description": "Dune is a 1965 epic science fiction novel by American author Frank Herbert", "price": 20}'`  

`curl -X PUT http://localhost:8081/consumer/products/13 -H "Content-Type: application/json" -d '{"name": "Dune", "description": "Dune is a 1965 epic science fiction novel by American author Frank Herbert", "price": 100}'`    

`curl -X POST http://localhost:8081/consumer/products -H "Content-Type: application/json" -d '{"name": "iPhone", "price": 100, "category": {"id": 1, "name": "electronics"}}'`  

`curl -X DELETE http://localhost:8081/consumer/products/1`  

`curl -X GET 'http://localhost:8081/consumer/categories'`   

`curl -X GET 'http://localhost:8081/consumer/categories/3'`