package com.example.consumerservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private CategoryDTO category;
}
