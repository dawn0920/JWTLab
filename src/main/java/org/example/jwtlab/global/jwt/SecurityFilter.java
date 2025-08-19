package org.example.jwtlab.global.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.jwtlab.domain.user.enums.UserRole;
import org.example.jwtlab.global.response.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url  = request.getRequestURI();

        if (url.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");
        if (bearerJwt == null) {
            sendErrorResponse(response, JwtErrorCode.JWT_MISSING);
            return;
        }

        try {
            String jwt = jwtUtil.subStringToketn(bearerJwt);
            Claims claims = jwtUtil.extractClaims(jwt);

            Long userId = Long.parseLong(claims.getSubject());
            UserRole userRole = UserRole.valueOf(claims.get("UserRole", String.class));

            UserAuth userAuth = new UserAuth(
                    userId,
                    claims.get("email", String.class),
                    "",
                    claims.get("name", String.class)
                    ,userRole
            );

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userAuth, null, userAuth.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, JwtErrorCode.JWT_INVALID);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format(
                "{\"status\": %d, \"code\": \"%s\", \"message\": \"%s\"}",
                errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()
        ));
    }
}
