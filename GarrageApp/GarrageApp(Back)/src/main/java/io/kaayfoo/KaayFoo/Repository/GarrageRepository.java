package io.kaayfoo.KaayFoo.Repository;

import org.springframework.data.repository.CrudRepository;

import io.kaayfoo.KaayFoo.Modele.Car;

public interface GarrageRepository extends CrudRepository<Car,Long> {

}
