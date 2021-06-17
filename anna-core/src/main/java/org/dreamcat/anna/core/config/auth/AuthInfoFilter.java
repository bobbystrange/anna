package org.dreamcat.anna.core.config.auth;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.web.security.AnonymityProperties;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Create by tuke on 2021/1/29
 */
@Component
@RequiredArgsConstructor
public class AuthInfoFilter extends OncePerRequestFilter {

    private final AnonymityProperties anonymityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        if (anonymityProperties.isAnonymous(request.getRequestURI())) {
            SecurityContextHolder.setContext(securityContext);
            filterChain.doFilter(request, response);
            return;
        }


        var cookies = request.getCookies();
        for (var cookie : cookies) {
            cookie.getName();
        }
    }
}
