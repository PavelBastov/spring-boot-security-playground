package io.juakali.security.config;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class RobotAuthenticationToken extends AbstractAuthenticationToken {

  public RobotAuthenticationToken() {
    super(AuthorityUtils.createAuthorityList("ROLE_robot"));
  }

  @Override
  public @Nullable Object getCredentials() {
    return null;
  }

  @Override
  public @Nullable Object getPrincipal() {
    return "I, Robot 🤖";
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    throw new RuntimeException("🎶 Can't touch this");
  }
}
