package com.inn.cafe.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name="Category.getAllCategory", query="select c from Category c where c.id in (select p.category from Product p where p.status = 'true')")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="category")
public class Category implements Serializable{
private final static Long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy =GenerationType.IDENTITY)
@Column(name = "id")
Integer id;

@Column(name= "name")
String name;

}
