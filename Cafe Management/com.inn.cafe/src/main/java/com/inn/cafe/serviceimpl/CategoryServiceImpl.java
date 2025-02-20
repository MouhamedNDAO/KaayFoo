package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.constants.CafeConstant;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.CafeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
     CategoryDao categoryDao;

    @Autowired
     JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
      try {
        if (jwtFilter.isAdmin()) {
            if (validateCategoryMap(requestMap,false)) {
                categoryDao.save(getCategoryFromMap(requestMap, false));
                return CafeUtil.getResponseEntity("Category added successfully", HttpStatus.OK);   
             }else{
                    return CafeUtil.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                   }
            }
        } catch (Exception e) {
                    e.printStackTrace();
         }
          return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
         }
            
         private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
             if (requestMap.containsKey("name")) {
              if (requestMap.containsKey("id") && validateId) {
                  return true;
              }else if (!validateId) {
                   return true;
                }
              }
                return false;
            }
        
        private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
            Category category = new Category();
            if (isAdd) {
                category.setId(Integer.parseInt(requestMap.get("id")));
            }
            category.setName(requestMap.get("name"));
            return category;
        }

        @Override
        public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
            try {
                if (!Strings.isNotEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                    log.info("Inside if");
                    return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
                }
                return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Override
        public ResponseEntity<String> update(Map<String, String> requestMap) {
            try {
                if (jwtFilter.isAdmin()) {
                    if (validateCategoryMap(requestMap, false)) {
                        Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                        
                        if (!optional.isEmpty()) {
                            categoryDao.save(getCategoryFromMap(requestMap,true));
                            return CafeUtil.getResponseEntity("Category updated successfully", HttpStatus.OK);
                        } else {
                            return CafeUtil.getResponseEntity("Category id does not exist", HttpStatus.OK);
                        } 
                    } 
                    return CafeUtil.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
                } else {
                    return CafeUtil.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
