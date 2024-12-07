package ru.spring.dbcourse.CityExpress.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;

public interface DriverRepository {
  List<Driver> getDrivers();

  Driver getDriverById(int id);

  void deleteDriver(int id);

  void createDriver(Driver driver);

  void addCar(int driverId, int car);
}
