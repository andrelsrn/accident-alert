package dev.andre.accidentalert.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.andre.accidentalert.dto.response.ErrorResponseDTO;
import dev.andre.accidentalert.entity.User;
import dev.andre.accidentalert.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow public endpoints to pass through without authentication
        if (path.startsWith("/auth") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeError(response, 401, "Unauthorized", "Token is missing or invalid", path);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByEmail(email).orElse(null);

            if(user == null){
                writeError(response, 401, "Unauthorized", "Token is missing or invalid", path);
                return;
            }


            if (!user.getActive()) {
                writeError(response, 403, "Forbidden", "User account is deactivated", path);
                return;
            }

            if (user.getMustChangePassword()) {

                boolean allowed =
                        path.contains("/auth/login") ||
                                path.contains("/users/password");

                if (!allowed) {
                    writeError(response, 403, "Forbidden",
                            "You must change your password before accessing other resources",
                            path);
                    return;
                }
            }

            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response,
                            int status,
                            String error,
                            String message,
                            String path) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponseDTO dto = new ErrorResponseDTO(
                LocalDateTime.now(),
                status,
                error,
                message,
                path
        );

        response.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}
