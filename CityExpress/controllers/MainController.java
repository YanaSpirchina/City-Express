package ru.spring.dbcourse.CityExpress.controllers;

import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.spring.dbcourse.CityExpress.config.UserService;
import ru.spring.dbcourse.CityExpress.models.Car;
import ru.spring.dbcourse.CityExpress.models.Driver;
import ru.spring.dbcourse.CityExpress.models.Journal;
import ru.spring.dbcourse.CityExpress.models.Route;
import ru.spring.dbcourse.CityExpress.service.CarService;
import ru.spring.dbcourse.CityExpress.service.DriverService;
import ru.spring.dbcourse.CityExpress.service.JournalService;
import ru.spring.dbcourse.CityExpress.service.RouteService;

@Controller
@RequestMapping("/city-express")
@AllArgsConstructor
public class MainController {
  private final DriverService driverService;
  private final CarService carService;
  private final RouteService routeService;
  private final JournalService jornalService;

  @Autowired
  private UserService userService;


  @GetMapping()
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public String mainPage(Model model) {
    String currentUsername = userService.getCurrentUsername();
    model.addAttribute("username", currentUsername);
    return "mainPage";
  }

  @GetMapping("/welcome")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public String welcomePage() {
    return "welcome"; // Страница приветствия доступна всем
  }

  @GetMapping("/drivers")
  @PreAuthorize("hasRole('ADMIN')")
  public String getDrivers(Model model) throws Exception {
    model.addAttribute("drivers", driverService.getDrivers());
    return "drivers/allDrivers";
  }

  @GetMapping("/cars")
  @PreAuthorize("hasRole('ADMIN')")
  public String getCars(Model model) {
    model.addAttribute("cars", carService.getCars());
    return "cars/allCars";
  }

  @GetMapping("/routes")
  @PreAuthorize("hasRole('ADMIN')")
  public String getRoutes(Model model) {
    model.addAttribute("routes", routeService.getRoutes());
    return "routes/allRoutes";
  }

  @GetMapping("/journal")
 @PreAuthorize("hasRole('ADMIN')")
  public String getJournal(Model model) {
    List<Car> carNull = carService.getCarsWhereTimeInIsNull();
    List<Car> carNotNUll = carService.getCarsWhereTimeInIsNotNull();
    model.addAttribute("carsTimeInNull", carService.getCarsWhereTimeInIsNull());
    model.addAttribute("carsTimeInNotNull", carService.getCarsWhereTimeInIsNotNull());
    return "journal/allJournal";
  }

  @GetMapping("/driver/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String getDriver(@PathVariable("id") int id, Model model) {
    List<Car> cars = carService.carDriverIsNull();
    Driver driver = driverService.getDriverById(id);
    model.addAttribute("cars", carService.getCarsBuPersonId(id));
    model.addAttribute("driver", driverService.getDriverById(id));
    model.addAttribute("carDriverNull", carService.carDriverIsNull());
    return "drivers/id";
  }

  @GetMapping("/car/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String getCar(@PathVariable("id") int id, Model model) {
    Driver driver = carService.getDriver(id);
    model.addAttribute("car", carService.getCarById(id));
    model.addAttribute("driver", carService.getDriver(id));
    return "cars/id";
  }

  @GetMapping("/routes/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String getRoute(@PathVariable("id") int id, Model model) {
    model.addAttribute("route", routeService.getRouteById(id));
    model.addAttribute("minTime", jornalService.getShortestTrip(routeService.getRouteById(id).getName()));
    return "routes/id";
  }

  @GetMapping("/journal/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String getJournal(@PathVariable("id") int id, Model model) {
    boolean j = jornalService.isCarInRoute(id);
    List<Car> d = carService.getAllInfoAboutCar(id);
    String f = jornalService.getRoute(id);
    model.addAttribute("allInfoCars", carService.getAllInfoAboutCar(id));
    model.addAttribute("isCarInRoute", jornalService.isCarInRoute(id));
    model.addAttribute("route", jornalService.getRoute(id));
    return "journal/id";
  }

  @PostMapping("/journal/return/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String returnCar(@PathVariable("id") int id) {
    jornalService.returnCar(id);
    return "redirect:/city-express/journal/" + id;
  }

  @PostMapping("/journal/send/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String sendCar(@PathVariable("id") int id) {
    jornalService.returnCar(id);
    return "redirect:/city-express/journal/" + id;
  }

  @GetMapping("/journal/release/{carId}/{driverId}")
  @PreAuthorize("hasRole('ADMIN')")
  public String sendCarButtom(@PathVariable("carId") int carId,
                              @PathVariable("driverId") int driverId, Model model) {
    model.addAttribute("car", carService.getCarById(carId));
    model.addAttribute("driver", driverService.getDriverById(driverId));
    model.addAttribute("routes", routeService.getRoutes());
    return "journal/newTrip";
  }

  @PatchMapping("/routes/{carId}/{driverId}/assign")
  @PreAuthorize("hasRole('ADMIN')")
  public String sendCarToTrip(@PathVariable("carId") int carId,
                              @PathVariable("driverId") int driverId,
                              @RequestParam("routeId") int routeId) {
    jornalService.sendCar(carId, driverId, routeId);
    return "redirect:/city-express/journal";
  }

  @DeleteMapping("/driver/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String deleteDriver(@PathVariable("id") int id) {
    driverService.deleteDriver(id);
    return "redirect:/city-express/drivers";
  }

  @GetMapping("/driver/new")
  @PreAuthorize("hasRole('ADMIN')")
  public String newDriver(Model model) {
    model.addAttribute("driver", new Driver());
    return "drivers/new";
  }

  @PostMapping("")
  @PreAuthorize("hasRole('ADMIN')")
  public String newDriver(@ModelAttribute("driver") @Valid Driver driver) {
    driverService.createDriver(driver);
    return "redirect:/city-express/drivers";
  }

  @DeleteMapping("/car/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String deleteCar(@PathVariable("id") int id) {
    carService.deleteCar(id);
    return "redirect:/city-express/cars";
  }

  @GetMapping("/car/new")
  @PreAuthorize("hasRole('ADMIN')")
  public String newCar(Model model) {
    model.addAttribute("car", new Car());
    return "cars/new";
  }

  @PostMapping("/driver/new")
  @PreAuthorize("hasRole('ADMIN')")
  public String newCar(@ModelAttribute("car") @Valid Car car) {
    carService.createCar(car);
    return "redirect:/city-express/cars";
  }

  @PatchMapping("/drivers/{driverId}/assign")
  @PreAuthorize("hasRole('ADMIN')")
  public String carAddToDriver(@PathVariable("driverId") int driverId,
                               @RequestParam("carId") int carId) {
    driverService.addCar(driverId, carId);
    return "redirect:/city-express/drivers";
  }

  @GetMapping("/route/new")
  @PreAuthorize("hasRole('ADMIN')")
  public String newRoute(Model model) {
    model.addAttribute("route", new Route());
    return "routes/new";
  }

  @PostMapping("/route/new")
  @PreAuthorize("hasRole('ADMIN')")
  public String createRoute(@ModelAttribute("route") @Valid Route route) {
    routeService.createRoute(route);
    return "redirect:/city-express/routes";
  }

  @DeleteMapping("/route/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String deleteRoute(@PathVariable("id") int id) {
    routeService.deleteRoute(id);
    return "redirect:/city-express/routes";
  }

  @GetMapping("/journal/downloadExcel")
  public ResponseEntity<Resource> downloadJournalExcel() {
    // Retrieve journal data from your database
    List<Journal> journalEntries = jornalService.findAllJournalEntries();

    // Convert journal data to Excel using Apache POI
    ByteArrayOutputStream outputStream = jornalService.convertJournalToExcel(journalEntries);

    // Create a ByteArrayResource from the Excel data
    ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

    // Set headers for the download response
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment().filename("journal.xlsx").build());

    // Return the Excel data as a ResponseEntity with headers
    return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
  }

}
