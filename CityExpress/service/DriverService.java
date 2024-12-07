package ru.spring.dbcourse.CityExpress.service;

import org.springframework.stereotype.Service;
import java.util.List;
import lombok.AllArgsConstructor;
import ru.spring.dbcourse.CityExpress.models.Driver;
import ru.spring.dbcourse.CityExpress.repository.DriverRepositoryImpl;

@Service
@AllArgsConstructor
public class DriverService {
  private final DriverRepositoryImpl driverRepositoryImpl;

  public List<Driver> getDrivers() {
    return driverRepositoryImpl.getDrivers();
  }

  public Driver getDriverById(int id) {
    return driverRepositoryImpl.getDriverById(id);
  }

  public void deleteDriver(int id) {
    driverRepositoryImpl.deleteDriver(id);
  }

  public void createDriver(Driver driver){
    driverRepositoryImpl.createDriver(driver);
  }

  public void addCar(int driverId, int car) {
    driverRepositoryImpl.addCar(driverId, car);
  }
}
