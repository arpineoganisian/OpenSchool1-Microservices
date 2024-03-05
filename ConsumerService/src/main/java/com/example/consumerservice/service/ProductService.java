package com.example.consumerservice.service;

import com.example.consumerservice.dto.ProductDTO;
import com.example.consumerservice.exception.InvalidRequestException;
import com.example.consumerservice.util.JacksonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ProductService {
//    здесь
//    private static final String SUPPLIER_SERVICE_URL = "http://supplier:8080/supplier/products";
    private static final String SUPPLIER_SERVICE_URL = "http://localhost:8080/supplier/products";

    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Page<ProductDTO> findAll(Integer minPrice, Integer maxPrice, String category,
                                    Integer pageNo, Integer pageSize) {

        if ((pageNo == null) ^ (pageSize == null))
            throw new InvalidRequestException("Both page and size should be specified or none of them.");
        if (pageNo != null && pageNo < 0)
            throw new InvalidRequestException("Page number cannot be negative");
        if (pageNo != null && pageSize <= 0)
            throw new InvalidRequestException("Page size cannot be less than or equal to zero");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SUPPLIER_SERVICE_URL);
        if (minPrice != null) builder.queryParam("min_price", minPrice);
        if (maxPrice != null) builder.queryParam("max_price", maxPrice);
        if (category != null) builder.queryParam("category", category);

        ResponseEntity<JacksonPage<ProductDTO>> response = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});

        return response.getBody();
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
