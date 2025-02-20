package com.inn.cafe.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.dao.BillDao;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BillDao billDao;

    @Autowired
    ProductDao productDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category",categoryDao.count());
        map.put("bill", billDao.count());
        map.put("product", productDao.count());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

}
