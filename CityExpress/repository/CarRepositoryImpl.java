package ru.spring.dbcourse.CityExpress.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;
import ru.spring.dbcourse.CityExpress.models.Journal;
import ru.spring.dbcourse.CityExpress.models.Route;

@Repository
@AllArgsConstructor
public class CarRepositoryImpl implements CarRepository {
  private final JdbcTemplate jdbcTemplate;


  @Override
  public List<Car> getCars() {
    return jdbcTemplate.query("SELECT * FROM car", new BeanPropertyRowMapper<>(Car.class));
  }

  @Override
  public Car getCarById(int id) {
    return jdbcTemplate.queryForObject("SELECT * FROM car where id = ?", new Object[] {id},
        new BeanPropertyRowMapper<>(Car.class));
  }

  @Override
  public List<Car> getCarsWhereTimeInIsNull() {
    List<Car> l = jdbcTemplate.query("SELECT * FROM cars_with_null_time_in ",
        new BeanPropertyRowMapper<>(Car.class));
    return l;
  }

  @Override
  public List<Car> getCarsWhereTimeInIsNotNull() {
    return jdbcTemplate.query("SELECT * FROM cars_without_null_time_in ",
        new BeanPropertyRowMapper<>(Car.class));
  }

  @Override
  public List<Car> getAllInfoAboutCar(int id) {
    return jdbcTemplate.query("SELECT * FROM get_car_info_by_id(?);", new Object[] {id},
        new CarRowMapper());
  }

  public void deleteCar(int id) {
    jdbcTemplate.update("DELETE from car where id=?", new Object[] {id});
  }

  @Override
  public Driver getDriver(int id) {
    List<Driver> drivers = jdbcTemplate.query(
        "SELECT * FROM driver JOIN car ON driver.id = car.driver_id WHERE car.id = ?",
        new Object[]{id},
        new BeanPropertyRowMapper<>(Driver.class)
    );

    return drivers.isEmpty() ? null : drivers.get(0);


  }

  private static class CarRowMapper implements RowMapper<Car> {
    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
      Car car = new Car();
      car.setId(rs.getInt("car_id_column"));
      car.setNum(rs.getString("num"));
      car.setColor(rs.getString("color"));
      car.setMark(rs.getString("mark"));

      Driver driver = new Driver();
      driver.setId(rs.getInt("driver_id"));
      driver.setFirstName(rs.getString("first_name"));
      driver.setLastName(rs.getString("last_name"));
      driver.setFatherName(rs.getString("father_name"));
      car.setOwner(driver);

      return car;
    }
  }

  @Override
  public List<Car> getCarsByPersonId(int id) {
    return jdbcTemplate.query(
        "SELECT c.id, c.num, c.color, " +
            "c.mark " +
            "FROM car c " +
            "JOIN driver d ON c.driver_id = d.id " +
            "WHERE d.id=?", new Object[] {id},
        new BeanPropertyRowMapper<>(
            Car.class));
  }

  @Override
  public void createCar(Car car) {
    jdbcTemplate.update("insert into car(num, color, mark) " +
        "values (?,?, ? )", car.getNum(), car.getColor(), car.getMark());
  }

  @Override
  public List<Car> carDriverIsNull() {
    return jdbcTemplate.query(
        "SELECT id, num, color, mark " +
            "FROM car " +
            "WHERE driver_id IS NULL;",
        new BeanPropertyRowMapper<>(
            Car.class));
  }
}
