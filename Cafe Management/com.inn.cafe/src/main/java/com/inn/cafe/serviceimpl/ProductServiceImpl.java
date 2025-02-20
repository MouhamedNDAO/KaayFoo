package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.POJO.Product;
import com.inn.cafe.constants.CafeConstant;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.CafeUtil;
import com.inn.cafe.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductDao productDao;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
       try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap,false)) {
                                    productDao.save( getProductFromMap(requestMap, false));
                                    return CafeUtil.getResponseEntity("Product added successfully", HttpStatus.OK);
                                }
                                return CafeUtil.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST); 
                            }else {
                                 return CafeUtil.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);   
                                }
                            
                       } catch (Exception e) {
                        e.printStackTrace();
                       }
                       return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                
    private boolean validateProductMap(Map<String,String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if(!validateId){
                return true;
            }
        }
        return false;
    }
    private Product getProductFromMap(Map<String,String> requestMap, boolean issAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("id")));
        Product product = new Product();
        if (issAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        product.setDescription(requestMap.get("description"));
        return product;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct(String filterValue) {
       try {
        return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
       } catch (Exception e) {
        e.printStackTrace();
       }
       return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CafeUtil.getResponseEntity("Product updated successfully", HttpStatus.OK);
                    }else{
                        return CafeUtil.getResponseEntity("Product id does not updated ", HttpStatus.OK);
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

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {

                   Optional<Product> optional = productDao.findById(id);

                   if (!optional.isEmpty()) {
                        productDao.deleteById(id);
                        return CafeUtil.getResponseEntity("Product deleted successfully", HttpStatus.OK);
                   } else {
                        return CafeUtil.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
                   }
                } else {
                    return CafeUtil.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(Integer.parseInt("id"));

                if (!optional.isEmpty()) {
                    productDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    CafeUtil.getResponseEntity("Product status updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtil.getResponseEntity("Product doesn't exist", HttpStatus.OK);
                }
            }
            return CafeUtil.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

}
