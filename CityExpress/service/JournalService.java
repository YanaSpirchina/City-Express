package ru.spring.dbcourse.CityExpress.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.spring.dbcourse.CityExpress.models.Journal;
import ru.spring.dbcourse.CityExpress.repository.DriverRepositoryImpl;
import ru.spring.dbcourse.CityExpress.repository.JournalRepositoryImpl;

@Service
@AllArgsConstructor
public class JournalService {
  private final JournalRepositoryImpl journalRepositoryImpl;
  private final JdbcTemplate jdbcTemplate;

  public boolean isCarInRoute(int id) {
    List<Journal> journal = journalRepositoryImpl.getJournalByCarId(id);
    if (journal.isEmpty()) {
      return false;
    }
    for (Journal j : journal) {
      if (j.getTimeIn() == null) {
        return true;
      }
    }
    return false;
  }

  public String getRoute(int id) {
    List<Journal> journal = journalRepositoryImpl.getJournalByCarId(id);
    if (journal.isEmpty()) {
      return "";
    }
    for (Journal j : journal) {
      if (j.getTimeIn() == null) {
        int routeId = jdbcTemplate.queryForObject(
            "SELECT route_id FROM journal WHERE id = ?",
            Integer.class,
            j.getId()
        );
        return jdbcTemplate.queryForObject(
            "SELECT name FROM routes WHERE id=?",
            String.class,
            routeId
        );


      }
    }
    return "";
  }

  public void returnCar(int id) {
    journalRepositoryImpl.returnCar(id);
  }

  public void sendCar(int carId, int driverId, int routeId) {
    journalRepositoryImpl.sendCar(carId, driverId, routeId);
  }

  public ByteArrayOutputStream convertJournalToExcel(List<Journal> journalEntries) {
    try (Workbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

      // Create a sheet
      Sheet sheet = workbook.createSheet("Journal");

      // Create header row
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("ID");
      headerRow.createCell(1).setCellValue("Time Out");
      headerRow.createCell(2).setCellValue("Time In");
      headerRow.createCell(3).setCellValue("Car Num");
      headerRow.createCell(4).setCellValue("Route Name");

      // Set column widths (optional)
      sheet.setColumnWidth(0, 256 * 10);
      sheet.setColumnWidth(1, 256 * 20);
      sheet.setColumnWidth(2, 256 * 20);
      sheet.setColumnWidth(3, 256 * 10);
      sheet.setColumnWidth(4, 256 * 10);

      // Add journal entries to rows
      int rowNum = 1;
      for (Journal entry : journalEntries) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(entry.getId());
        row.createCell(1).setCellValue(entry.getTimeOut());
        row.createCell(2).setCellValue(entry.getTimeIn());
        row.createCell(3).setCellValue(entry.getCar().getNum());
        row.createCell(4).setCellValue(entry.getRoute().getName());
      }

      // Write the workbook to the output stream
      workbook.write(outputStream);

      return outputStream;
    } catch (IOException e) {
      throw new RuntimeException("Error converting journal to Excel: " + e.getMessage(), e);
    }
  }

  public List<Journal> findAllJournalEntries() {

    String sql = "SELECT j.id, j.time_out, j.time_in, c.num as car_num, r.name as route_name " +
        "FROM journal j " +
        "JOIN car c ON j.car_id = c.id " +
        "JOIN routes r ON j.route_id = r.id";

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      int id = rs.getInt("id");
      Date timeOut = rs.getTimestamp("time_out");
      Date timeIn = rs.getTimestamp("time_in");
      String carNum = rs.getString("car_num");
      String routeName = rs.getString("route_name");
      return new Journal(id, timeOut, timeIn, carNum, routeName);
    });
  }

  public List<String> getShortestTrip(String route) {
    return journalRepositoryImpl.getShortestTrip(route);
  }
}
