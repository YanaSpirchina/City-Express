package ru.spring.dbcourse.CityExpress.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DriverRepositoryImpl implements DriverRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Driver> getDrivers() {
    return jdbcTemplate.query("SELECT * FROM driver", new BeanPropertyRowMapper<>(Driver.class));
  }

  @Override
  public Driver getDriverById(int id) {
    return jdbcTemplate.queryForObject("SELECT * FROM driver where id = ?", new Object[] {id},
        new BeanPropertyRowMapper<>(Driver.class));
  }

  @Override
  public void deleteDriver(int id) {
    jdbcTemplate.update("UPDATE car " +
        "SET driver_id = NULL, time_in= NOW() " +
        "WHERE driver_id = ?", id);

    jdbcTemplate.update("DELETE from driver where id=?", new Object[] {id});
  }

  @Override
  public void createDriver(Driver driver) {
    jdbcTemplate.update("insert into driver(first_name, last_name, father_name) " +
        "values (?,?, ? )", driver.getFirstName(), driver.getLastName(), driver.getFatherName());
  }

  @Override
  public void addCar(int driverId, int car) {
    jdbcTemplate.update("update car set driver_id=? where id=?", driverId, car);
  }
}
