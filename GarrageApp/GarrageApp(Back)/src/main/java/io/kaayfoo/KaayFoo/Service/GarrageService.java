package io.kaayfoo.KaayFoo.Service;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.kaayfoo.KaayFoo.Modele.Car;
//import io.kaayfoo.KaayFoo.Modele.Car.Color;
import io.kaayfoo.KaayFoo.Repository.GarrageRepository;

@Service
public class GarrageService {
   
    
    @Autowired
    GarrageRepository garrageRepository;

    public List<Car> getCars(){
        List<Car> cars = new ArrayList<>();
        garrageRepository.findAll().forEach(car -> {
            cars.add(car);
        });
        return cars ;
    }

    public Car getCar(long id) {
      return garrageRepository.findById(id).orElse(null);
    }
    
    public void deleteCar(long id ){
       garrageRepository.deleteById(id);
       
    }

    public void addCar(Car car) {
        garrageRepository.save(car);
    }

    public void updateCar(Car car, long id) {
        garrageRepository.save(car);
    };

}
