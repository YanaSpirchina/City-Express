package ru.spring.dbcourse.CityExpress.repository;

import java.util.List;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;

public interface CarRepository {
  List<Car> getCars();

  Car getCarById(int id);

  List<Car> getCarsWhereTimeInIsNull();

  List<Car> getCarsWhereTimeInIsNotNull();

  List<Car> getAllInfoAboutCar(int id);

  List<Car> getCarsByPersonId(int id);

  void deleteCar(int id);

  void createCar(Car car);

  List<Car> carDriverIsNull();

  Driver getDriver(int id);
}
