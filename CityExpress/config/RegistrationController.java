package ru.spring.dbcourse.CityExpress.config;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register"; // имя шаблона Thymeleaf для отображения формы регистрации
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                                      BindingResult bindingResult, Model model) {
        if (userService.existsByUsername(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.user.exists", "Пользователь с таким именем уже существует");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.save(userDto);
        }catch (Exception e){
            model.addAttribute("userExists", true);
            return "register";
        }
        return "redirect:/login";
    }
}
