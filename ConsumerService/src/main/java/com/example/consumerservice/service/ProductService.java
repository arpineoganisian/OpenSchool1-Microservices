package com.example.consumerservice.service;

import com.example.consumerservice.dto.ProductDTO;
import com.example.consumerservice.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ProductService {

    private static final String SUPPLIER_SERVICE_URL = "http://localhost:8080/supplier/products";

    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDTO> findAll(Integer minPrice, Integer maxPrice, String category,
                                    Integer pageNo, Integer pageSize) {

        if ((pageNo == null) ^ (pageSize == null)) {
            throw new InvalidRequestException("Both page and size should be specified or none of them.");
        }

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

        List<ProductDTO> filteredProducts = response.getBody().stream()
                .filter(filterPredicate)
                .toList();

        if (!filteredProducts.isEmpty() && pageNo != null && pageSize != null) {
            if (pageNo < 0) { throw new InvalidRequestException("Page number cannot be negative");}
            if (pageSize <= 0) { throw new InvalidRequestException("Page size cannot be less than or equal to zero");}
            filteredProducts = filteredProducts.stream()
                    .skip((long) pageNo * pageSize)
                    .limit(pageSize)
                    .toList();
        }
        return filteredProducts;
    }

    public ProductDTO findById(Long id) {
        ResponseEntity<ProductDTO> response = restTemplate
                .getForEntity(SUPPLIER_SERVICE_URL + "/" + id, ProductDTO.class);
        return response.getBody();
    }

    public List<ProductDTO> findByName(String name) {
        ResponseEntity<List<ProductDTO>> response = restTemplate
                .exchange(SUPPLIER_SERVICE_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});
        if (response.getBody() == null) {
            return List.of();
        }
        return response.getBody().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public ProductDTO save(ProductDTO product) {
        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(SUPPLIER_SERVICE_URL, product, ProductDTO.class);
        return response.getBody();
    }

    public void update(ProductDTO product, Long id) {
        product.setId(id);
        try {
            restTemplate.put(SUPPLIER_SERVICE_URL + "/" + id, product);
        } catch (HttpClientErrorException e) {
            throw new InvalidRequestException("Product with id " + id + " does not exist");
        }
    }

    public void deleteById(Long id) {
        try {
            restTemplate.delete(SUPPLIER_SERVICE_URL + "/" + id);
        } catch (HttpClientErrorException e) {
            throw new InvalidRequestException("Product with id " + id + " does not exist");
        }
    }
}
