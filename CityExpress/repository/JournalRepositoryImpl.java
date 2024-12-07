package ru.spring.dbcourse.CityExpress.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.spring.dbcourse.CityExpress.models.Journal;

@Repository
@AllArgsConstructor
public class JournalRepositoryImpl implements JournalRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Journal> getJournalByCarId(int id) {
    return jdbcTemplate.query("Select * from journal where car_id=?", new Object[] {id},
        new BeanPropertyRowMapper<>(
            Journal.class));
  }

  @Override
  public void returnCar(int id) {
    jdbcTemplate.update("update journal set time_in = now() where  time_in is null and car_id=?",
        new Object[] {id});
  }

  @Override
  public void sendCar(int carId, int driverId, int routeId) {
    jdbcTemplate.update(
        "insert into journal(time_out, time_in, car_id, route_id) values (now(),null, ?, ?)",
        carId, routeId);
  }

  @Override
  public List<String> getShortestTrip(String route) {
    List<String> result = new ArrayList<>();

    // Объединение данных из разных таблиц с использованием LEFT JOIN
    List<Map<String, Object>> trips = jdbcTemplate.queryForList(
        "SELECT " +
            " r.name AS routeName, " +
            " a.num AS carNum, " +
            " MIN(j.time_in - j.time_out) AS minDuration " +
            "FROM journal j " +
            "LEFT JOIN routes r ON r.id = j.route_id " +
            "LEFT JOIN car a ON a.id = j.car_id " +
            "WHERE r.name = ? AND j.time_in IS NOT NULL " +
            "GROUP BY r.name, a.num " +
            "ORDER BY minDuration " +
            "LIMIT 1", route);

    if (!trips.isEmpty()) {
      Map<String, Object> shortestTrip = trips.get(0);

      // Проверка null-значений и добавление данных в результат
      result.add((String) shortestTrip.get("routeName"));
      result.add((String) shortestTrip.get("carNum"));
      result.add(String.valueOf(shortestTrip.get("minDuration")));
    } else {
      // Обработка ситуации, когда маршрут не найден
      result.add("Маршрут не найден");
    }

    return result;
  }

}
