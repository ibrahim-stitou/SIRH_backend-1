package com.tarmiz.SIRH_backend.config;

import com.tarmiz.SIRH_backend.model.DTO.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            writeError(response, HttpStatus.UNAUTHORIZED, "JWT token expired");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        ErrorResponse error = new ErrorResponse(status.value(), message);
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(String.format("{\"status\":%d,\"message\":\"%s\"}", error.getStatus(), error.getMessage()));
    }
}