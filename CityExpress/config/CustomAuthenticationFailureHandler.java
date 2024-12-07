package ru.spring.dbcourse.CityExpress.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException exception) throws IOException, jakarta.servlet.ServletException {
        String errorMessage;

        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            errorMessage = "Username not found.";
            log.info("onAuthenticationFailure: Username not found.");
        } else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            errorMessage = "Invalid username or password.";
            log.info("onAuthenticationFailure: Invalid username or password.");
        } else {
            errorMessage = "Authentication failed.";
            log.info("onAuthenticationFailure: Authentication failed.");

        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/login?error=true");
    }



}
