package io.kaayfoo.KaayFoo.Controller;

import org.springframework.web.bind.annotation.RestController;

import io.kaayfoo.KaayFoo.Modele.Car;
//import io.kaayfoo.KaayFoo.Modele.Car.Color;
import io.kaayfoo.KaayFoo.Service.GarrageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@RestController
public class GarrageController {

 @Autowired 
 private GarrageService garrageService;

 //Appel la classe GarageService afin d'y recuperer toutes les cars stockees sous forme de liste avec la methode List<>
 
 @RequestMapping("/Cars")
 public List<Car> GetCars() {
    return garrageService.getCars();
 }

 //Recuper cette fois une seule car par le biais de son id 

 @RequestMapping("/Car/{id}")
 public Car getCar(@PathVariable long id){
    return garrageService.getCar(id);
 }

 //Supprimer un cars par le biais de son id

 @RequestMapping(method = RequestMethod.DELETE, value = "/Car/{id}")
 public void deleteCar(@PathVariable long id){
     garrageService.deleteCar(id);
 }

 //Ajout d'un car et il doit avoir les memes parametres que le addCar() dans service comme pour chaque methode 

 @RequestMapping(method = RequestMethod.POST,value = "/Cars")
 public void addCar(@RequestBody Car car){
     garrageService.addCar(car);
 }

 //Modifier un car

 @RequestMapping(method = RequestMethod.PUT, value = "/Car/{id}")
 public void updateCar(@RequestBody Car car,@PathVariable long id){
  garrageService.updateCar(car,id);
}
}
