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

@NamedQuery(name="User.findByEmailId", query="select u from User u where u.email= :email")
@NamedQuery(name="User.getAllUser", query="select new com.inn.cafe.wrapper.UserWrapper(u.id,u.name,p.email,p.contactNumber,p.status) from User u where u.role= 'user'")
@NamedQuery(name="User.updateStatus", query="update User u set u.status=:status where u.id= :id")
@NamedQuery(name="User.getAllAdmin", query="select u.email from User u where u.role= 'admin'")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User implements Serializable{

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="contactNumber")
    private String contactNumber;

    @Column(name="email")
    private String email;
    
    @Column(name="password")
    private String password;

    @Column(name="status")
    private String status;

    @Column(name="role")
    private String role;



}
