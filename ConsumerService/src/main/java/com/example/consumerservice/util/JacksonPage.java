package com.example.consumerservice.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties("pageable")
public class JacksonPage<T> extends PageImpl<T> {
    private JacksonPage(List<T> content, int number, int size, long totalElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}
