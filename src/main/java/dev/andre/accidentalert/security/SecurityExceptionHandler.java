package dev.andre.accidentalert.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.andre.accidentalert.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.LocalDateTime;

@Configuration
public class SecurityExceptionHandler {

    /**
     * 403 - User is authenticated but does not have the necessary permissions to access the resource.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {

            ErrorResponseDTO error = new ErrorResponseDTO(
                    LocalDateTime.now(),
                    403,
                    "Access Denied",
                    "You do not have permission to access this resource",
                    request.getRequestURI()
            );

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {

            ErrorResponseDTO error = new ErrorResponseDTO(
                    LocalDateTime.now(),
                    401,
                    "Unauthorized",
                    "Authentication is required",
                    request.getRequestURI()
            );

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        };
    }

}
