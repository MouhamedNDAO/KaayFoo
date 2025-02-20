package io.kaayfoo.KaayFoo.Modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//Variables
@Entity
public class Car {
    public enum Color {
        Red,
        Green,
        Blue,
        Orange,
        Yellow
    }
    @Id
    private long id ;
    private String modele;
    private String brand;
    @Column (name ="release_year")
    private int year;
    public Color color;

    public Car() {
        
    }
    
    //Constructeurs
    public Car(long id ,String modele,String brand,int year,Color color){
        super();
        this.modele= modele;
        this.id = id;
        this.brand= brand;
        this.year= year;
        this.color= color;
    }

//Getter
public long getId() {
    return id;
}
 public String getBrand() {
     return brand;
 }
 public Color getColor() {
     return color;
 }
 public String getModele() {
     return modele;
 }
 public int getYear() {
     return year;
 }

//Setters
 public void setId(long id) {
     this.id = id;
 }
 public void setBrand(String brand) {
     this.brand = brand;
 }
 public void setColor(Color color) {
     this.color = color;
 }
 public void setModele(String modele) {
     this.modele = modele;
 }
 public void setYear(int year) {
     this.year = year;
 }
}
