package com.learn.linkShortener.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("method", httpRequest.getMethod());
            MDC.put("path", httpRequest.getRequestURI());
            MDC.put("ip", httpRequest.getRemoteAddr());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null
                    && auth.isAuthenticated()
                    && !(auth instanceof AnonymousAuthenticationToken)) {
                MDC.put("user", auth.getName());
            } else {
                MDC.put("user", "anonymous");
            }

            chain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}
