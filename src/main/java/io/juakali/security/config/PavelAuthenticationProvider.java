package io.juakali.security.config;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

public class PavelAuthenticationProvider implements AuthenticationProvider {

  @Override
  public @Nullable Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    if (Objects.equals(authentication.getName(), "pavel")) {
      var pavel = User.withUsername("pavel")
          .password("ignored")
          .roles("user", "admin")
          .build();
      return UsernamePasswordAuthenticationToken.authenticated(
          pavel,
          null,
          pavel.getAuthorities()
      );
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
