package ru.spring.dbcourse.CityExpress.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "car")
@Data
public class Car {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "num")
  private String num;

  @Column(name = "color")
  private String color;

  @Column(name = "mark")
  private String mark;

  @ManyToOne
  @JoinColumn(name = "driver_id", referencedColumnName = "id")
  private Driver owner;

  @OneToMany(mappedBy = "car")
  private List<Journal> journal;

  public String getFullName() {
    return this.mark + " " + this.num;
  }

}
