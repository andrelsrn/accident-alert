package dev.andre.accidentalert.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityExceptionHandler {

    /**
     * 403 - User is authenticated but does not have the necessary permissions to access the resource.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            response.getWriter().write("""
                {
                    "status": 403,
                    "error": "Access Denied",
                    "message": "You do not have permission to access this resource",
                    "path": "%s"
                }
            """.formatted(request.getRequestURI()));
        };
    }

    /**
     * 401 - User is not authenticated and needs to log in to access the resource.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write("""
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "Authentication is required",
                    "path": "%s"
                }
            """.formatted(request.getRequestURI()));
        };
    }

}
