package io.juakali.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class ProhibitedFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    if (Objects.equals(request.getHeader("x-prohibited"), "yes")) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setCharacterEncoding("UTF-8");
      response.setHeader("ContentType", "text/plain;charset=UTF-8");
      response.getWriter().write("⛔⛔⛔⛔️ Prohibited");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
