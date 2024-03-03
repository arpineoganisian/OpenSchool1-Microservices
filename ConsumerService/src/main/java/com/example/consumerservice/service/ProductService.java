package com.example.consumerservice.service;

import com.example.consumerservice.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ProductService {

    private static final String SUPPLIER_SERVICE_URL = "http://localhost:8080/products";

    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    public List<ProductDTO> findAll() {
//        ResponseEntity<List<ProductDTO>> response = restTemplate
//                .exchange(SUPPLIER_SERVICE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
//        return response.getBody();
//    }

    public List<ProductDTO> findAll(Integer minPrice, Integer maxPrice, String category) {
        ResponseEntity<List<ProductDTO>> response = restTemplate
                .exchange(SUPPLIER_SERVICE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        List<ProductDTO> products = response.getBody();

        if (products == null) {
            return List.of();
        }

        Predicate<ProductDTO> filterPredicate = product -> {
            boolean minPriceCondition = minPrice == null || product.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0;
            boolean maxPriceCondition = maxPrice == null || product.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0;
            boolean categoryCondition = category == null || product.getCategory().getName().equals(category);
            return minPriceCondition && maxPriceCondition && categoryCondition;
        };

        // Фильтруем продукты с использованием предиката
        return response.getBody().stream()
                .filter(filterPredicate)
                .toList();
    }

    public ProductDTO findById(Long id) {
        ResponseEntity<ProductDTO> response = restTemplate
                .getForEntity(SUPPLIER_SERVICE_URL + id, ProductDTO.class);
        return response.getBody();
    }

    public List<ProductDTO> findByName(String name) {
        ResponseEntity<List<ProductDTO>> response = restTemplate
                .exchange(SUPPLIER_SERVICE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        return response.getBody().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public void save(ProductDTO product) {
        restTemplate.postForEntity(SUPPLIER_SERVICE_URL, product, ProductDTO.class);
    }

    public void update(ProductDTO product) {
        restTemplate.put(SUPPLIER_SERVICE_URL + product.getId(), product);
    }

    public void deleteById(Long id) {
        restTemplate.delete(SUPPLIER_SERVICE_URL + id);
    }
}
// TODO написать сервисы с интерфейсом
//  service
//  |___impl
//      |___ProductServiceImpl (class)
//      |___CategoryServiceImpl (class)
//  |___ProductService (interface
//  |___CategoryService (interface)
