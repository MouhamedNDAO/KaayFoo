package com.inn.cafe.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {

 Integer id;
 String name;
 String description;
 String price;
 String status;
 Integer categoryId;
 String categoryName;

}
