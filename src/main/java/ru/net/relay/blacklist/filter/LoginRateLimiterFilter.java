package ru.net.relay.blacklist.filter;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginRateLimiterFilter implements Filter {

    private static final int MAX_ATTEMPTS = 5;
    private final Cache<String, Integer> loginAttemptCache;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        if ("/rest/login".equals(req.getRequestURI())) {
            String ip = req.getRemoteAddr();
            Integer attempts = loginAttemptCache.getIfPresent(ip);

            if (attempts != null && attempts >= MAX_ATTEMPTS) {
                // Не выдавать сработку лимита
                ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
                log.warn("Wrong login attempts from {}", ip);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}