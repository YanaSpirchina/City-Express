package ru.spring.dbcourse.CityExpress.repository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.spring.dbcourse.CityExpress.models.Route;

@Repository
@AllArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Route> getRoutes() {
    return jdbcTemplate.query("SELECT * FROM routes", new BeanPropertyRowMapper<>(Route.class));
  }

  @Override
  public Route getRouteById(int id) {
    return jdbcTemplate.queryForObject("SELECT * FROM routes where id = ?", new Object[] {id},
        new BeanPropertyRowMapper<>(Route.class));
  }

  @Override
  public void createRoute(Route route) {
    jdbcTemplate.update("insert into routes(name) values(?)", route.getName());
  }

  @Override
  public void deleteRoute(int id) {
    jdbcTemplate.update("delete from routes where id=?", id);
  }
}
