package io.kaayfoo.KaayFoo.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import io.kaayfoo.KaayFoo.Modele.Car;
import io.kaayfoo.KaayFoo.Modele.Car.Color;

@Service
public class GarrageService {
    static private ArrayList<Car> cars = new ArrayList<>(Arrays.asList(
        new Car(1,"Fusion","Ford",2015,Color.Blue),
        new Car(2,"Focus","Ford",2013,Color.Red),
        new Car(3,"X6","BMW",2021,Color.Green),
        new Car(4,"GLE203","Mercedes",2018,Color.Blue),
        new Car(5,"Megane","Renault",2015,Color.Orange),
        new Car(6,"Avensis","Toyota",2016,Color.Blue),
        new Car(7,"Rogue","Nissan",2019,Color.Red),
        new Car(8,"Fiesta","Ford",2012,Color.Yellow)
    ));
    

    public List<Car> getCars(){
        return cars;
    }

    public Car getCar(long id) {
       return cars.stream().filter(car -> car.getId()==id).findFirst().orElse(null);
    }
    
    public void deleteCar(long id ){
       cars.removeIf(car -> car.getId()== id);
       
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void updateCar(Car car, long id) {
        cars.forEach(car1 -> {if (car1.getId() == id) {
            cars.set(cars.indexOf(car1),car);
        }
    });
    };

}
