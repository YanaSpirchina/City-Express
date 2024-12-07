package ru.spring.dbcourse.CityExpress.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "driver")
@Data
public class Driver {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "father_name")
  private String fatherName;

  @OneToMany(mappedBy = "owner")
  private List<Car> cars;

  public String getFullName() {
    return this.lastName + " " + this.firstName + " " + this.fatherName;
  }
}
