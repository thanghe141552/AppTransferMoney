package com.example.security;

import com.example.entity.Role;
import com.example.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
//            this.logger.trace("Did not process authentication request since failed to find "
//                    + "token in Basic Authorization header");
            chain.doFilter(request, response);
            return;
        }
        authHeader = authHeader.trim();
//        this.logger.trace(LogMessage.format("Found token '%s' in Authorization header", authHeader));
        if (!StringUtils.startsWithIgnoreCase(authHeader, "Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        if (authHeader.equalsIgnoreCase("Bearer")) {
            throw new BadCredentialsException("Empty JWT token");
        }

        String token = authHeader.substring(7);
        Claims claims = JwtUtils.validateToken(token);
        assert claims != null;
        String uid = claims.getSubject();
        List<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role", String.class).split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(uid, token, authorities));
        chain.doFilter(request, response);
    }
}
