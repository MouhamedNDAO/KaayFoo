package com.inn.cafe.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/dashboard")
public interface DashboarRest {
 
    @GetMapping(path = "/details")
    ResponseEntity<Map<String, Object>> getCount();
}
