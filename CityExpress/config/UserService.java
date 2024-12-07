package ru.spring.dbcourse.CityExpress.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcUserDetailsManager userDetailsManager;
    private final BCryptPasswordEncoder passwordEncoder;
    public void save(UserRegistrationDto userDto) {
        // Проверяем, что пароли совпадают
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords are not equality");
        }

        // Проверяем, что пользователя с таким именем не существует
        if (userDetailsManager.userExists(userDto.getUsername())) {
            throw new UsernameNotFoundException("Пользователь с таким именем уже существует");
        }

        // Создаем объект UserDetails с зашифрованным паролем
        UserDetails user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles("USER") // Роль пользователя по умолчанию
                .build();

        // Сохраняем пользователя в базе данных
        userDetailsManager.createUser(user);
    }

    public boolean existsByUsername(String username) {
        return false;
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null; // Or throw an exception if the principal is not a UserDetails
        }
    }

}
