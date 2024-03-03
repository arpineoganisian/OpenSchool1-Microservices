package com.example.consumerservice.controller;

import com.example.consumerservice.dto.CategoryDTO;
import com.example.consumerservice.dto.ProductDTO;
import com.example.consumerservice.service.CategoryService;
import com.example.consumerservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

//Возможность добавления нового продукта с указанием категории - ??
//Реализовать возможность фильтрации продуктов по различным критериям, таким как цена, категория и т. д.
//Реализовать пагинацию для списка продуктов.
//Реализовать валидацию данных перед отправкой запросов на сервер.
//Обработать возможные ошибки при взаимодействии с API микросервиса-источника данных.
@RestController
@RequestMapping("/api/consumer/")
public class ConsumerController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ConsumerController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "category", required = false) String category
    ) {
        System.out.println();
        return ResponseEntity.ok(productService.findAll(minPrice, maxPrice, category)); //200
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id)); //200
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDTO>> getProductByName(@RequestParam String name) {
        List<ProductDTO> products = productService.findByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll()); //200

    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id)); //200
    }

    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDTO product) {
        productService.save(product);
        URI location = URI.create("/api/consumer/products/" + product.getId());
        return ResponseEntity.created(location).build(); //201
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        //TODO перенести set id в сервис
        product.setId(id);
        productService.update(product);
        return ResponseEntity.accepted().build(); //202
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build(); //204
    }
}
