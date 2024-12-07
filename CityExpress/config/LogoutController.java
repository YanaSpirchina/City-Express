package ru.spring.dbcourse.CityExpress.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";  // Перенаправляем на страницу логина после выхода
    }
}

