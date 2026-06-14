package io.juakali.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class RobotAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    // 1. Decide whether we want to apply the filter
    if (!Collections.list(request.getHeaderNames()).contains("x-robot-secret")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 2. Check credentials and authenticate or reject
    if (!Objects.equals(request.getHeader("x-robot-secret"), "beep-boop")) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setCharacterEncoding("UTF-8");
      response.setHeader("Content-Type", "text/plain;charset=UTF-8");
      response.getWriter().write("⛔⛔🤖🤖 You are not a good robot!");
      return;
    }

    var auth = new RobotAuthenticationToken();
    var newContext = SecurityContextHolder.createEmptyContext();
    newContext.setAuthentication(auth);
    SecurityContextHolder.setContext(newContext);

    // 3. Call next
    filterChain.doFilter(request, response);

    // 4. Cleanup, but no cleanup
  }
}
