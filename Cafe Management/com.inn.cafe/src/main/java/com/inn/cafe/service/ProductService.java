package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.wrapper.ProductWrapper;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String,String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProduct(String filterValue);

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<ProductWrapper> getProductById(Integer id);

    ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id);

}
