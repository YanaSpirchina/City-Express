package ru.spring.dbcourse.CityExpress.repository;

import java.util.List;
import ru.spring.dbcourse.CityExpress.models.Journal;

public interface JournalRepository {
  List<Journal> getJournalByCarId(int id);
  void returnCar(int id);
  void sendCar(int carId, int driverId, int routeId);

  List<String> getShortestTrip(String route);
}
