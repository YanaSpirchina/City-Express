package ru.spring.dbcourse.CityExpress.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "journal")
@Data
public class Journal {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "time_out")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeOut;

  @Column(name = "time_in")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeIn;

  @ManyToOne
  @JoinColumn(name = "route_id", referencedColumnName = "id")
  private Route route;

  @ManyToOne
  @JoinColumn(name = "car_id", referencedColumnName = "id")
  private Car car;

  public Journal(int id, Date timeOut, Date timeIn, String carNum, String routeName) {
    this.id = id;
    this.timeOut = timeOut;
    this.timeIn = timeIn;
    this.car = new Car();
    car.setNum(carNum);
    this.route = new Route();
    this.route.setName(routeName);
  }

  public Journal() {

  }

  public int getCarId() {
    return car.getId();
  }


  public double getRouteId() {
    return route.getId();
  }
}
