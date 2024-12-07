package ru.spring.dbcourse.CityExpress.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.AllArgsConstructor;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;
import ru.spring.dbcourse.CityExpress.repository.CarRepositoryImpl;

@Service
@AllArgsConstructor
public class CarService {
  private final CarRepositoryImpl carRepositoryImpl;

  private final JdbcTemplate jdbcTemplate;

  public List<Car> getCars() {
    return carRepositoryImpl.getCars();
  }

  public Car getCarById(int id) {
    return carRepositoryImpl.getCarById(id);
  }

  public List<Car> getCarsWhereTimeInIsNull() {
    return carRepositoryImpl.getCarsWhereTimeInIsNull();
  }

  public List<Car> getCarsWhereTimeInIsNotNull() {
    List<Car> result = carRepositoryImpl.getCarsWhereTimeInIsNotNull();
    List<Car> carsNoInJournal = jdbcTemplate.query("SELECT * FROM car " +
        "WHERE id NOT IN (SELECT car_id FROM journal)", new BeanPropertyRowMapper<>(Car.class));
    for (Car car : carsNoInJournal) {
      result.add(car);
    }
    return result;
  }

  public List<Car> getAllInfoAboutCar(int id) {
    return carRepositoryImpl.getAllInfoAboutCar(id);
  }

  public List<Car> getCarsBuPersonId(int id) {
    return carRepositoryImpl.getCarsByPersonId(id);
  }

  public void deleteCar(int id) {
    carRepositoryImpl.deleteCar(id);
  }

  public void createCar(Car car) {
    carRepositoryImpl.createCar(car);
  }

  public List<Car> carDriverIsNull() {
    return carRepositoryImpl.carDriverIsNull();
  }

  public Driver getDriver(int id) {
    return carRepositoryImpl.getDriver(id);
  }
}
