package ru.spring.dbcourse.CityExpress.repository;

import java.util.List;
import ru.spring.dbcourse.CityExpress.models.Route;

public interface RouteRepository {
  List<Route> getRoutes();

  Route getRouteById(int id);

  void createRoute(Route route);

  void deleteRoute(int id);
}
