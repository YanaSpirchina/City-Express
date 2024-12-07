package ru.spring.dbcourse.CityExpress.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("user"))
            .roles("USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        if (users.userExists(user.getUsername())) {
            users.deleteUser(user.getUsername());
        }

        if (users.userExists(admin.getUsername())) {
            users.deleteUser(admin.getUsername());
        }


        users.createUser(user);
        users.createUser(admin);

        return users;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .exceptionHandling(exception ->
                exception.accessDeniedHandler(customAccessDeniedHandler)// Ваш кастомный обработчик
            )
            .formLogin(form -> form
                .loginPage("/login") // Указываем страницу логина
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll() // Разрешаем доступ к странице логина всем
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL для выхода
                .logoutSuccessUrl("/login") // URL после успешного выхода
                .invalidateHttpSession(true) // Инвалидировать сессию
                .deleteCookies("JSESSIONID") // Удалить куки с идентификатором сессии
                .permitAll() // Разрешаем всем доступ к этому URL
            )
            .authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers(HttpMethod.GET, "/city-express").hasAnyRole("ADMIN", "USER")
                    .requestMatchers( "/city-express/journal/{id}").hasRole("ADMIN")
                    .requestMatchers( "/city-express/drivers").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/cars").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/routes").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/journal").hasRole("ADMIN")
                    .requestMatchers( "/city-express/driver/{id}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/car/{id}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/routes/{id}").hasAnyRole("USER", "ADMIN")
                    .requestMatchers( "/city-express/journal/release/{carId}/{driverId}").hasRole("ADMIN")
                    .requestMatchers( "/city-express/driver/new").hasRole("ADMIN")
                    .requestMatchers( "/city-express/car/new").hasRole("ADMIN")
                    .requestMatchers( "/city-express/route/new").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/welcome").hasAnyRole("ADMIN", "USER")


                    .requestMatchers(HttpMethod.GET, "/register").permitAll() // Доступ к регистрации разрешен всем
                    .requestMatchers(HttpMethod.POST, "/register").permitAll() // Разрешаем POST запросы на регистрацию

                    .requestMatchers("/").hasAnyRole("ADMIN", "USER")


                    .anyRequest().authenticated()

            )
            .build();


    }

}