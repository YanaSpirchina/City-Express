package ru.spring.dbcourse.CityExpress.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spring.dbcourse.CityExpress.models.Driver;
import ru.spring.dbcourse.CityExpress.models.Route;
import ru.spring.dbcourse.CityExpress.repository.DriverRepositoryImpl;
import ru.spring.dbcourse.CityExpress.repository.RouteRepositoryImpl;

@Service
@AllArgsConstructor
public class RouteService {
  private final RouteRepositoryImpl routeRepositoryImpl;

  public List<Route> getRoutes() {
    return routeRepositoryImpl.getRoutes();
  }

  public Route getRouteById(int id) {
    return routeRepositoryImpl.getRouteById(id);
  }

  public void createRoute(Route route) {
    routeRepositoryImpl.createRoute(route);
  }

  public void deleteRoute(int id) {
    routeRepositoryImpl.deleteRoute(id);
  }
}
