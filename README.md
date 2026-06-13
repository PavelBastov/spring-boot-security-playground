# Spring Security Playground

A minimal Spring Boot 4 web app for experimenting with **Spring Security**. Server-side
pages rendered with Thymeleaf, form-based login, and role-based access control backed by
in-memory users — small enough to read in one sitting, structured enough to extend.

## Stack

| | |
|---|---|
| Spring Boot | 4.1.0 |
| Java | 25 (Temurin) |
| Build | Maven (wrapper included) |
| Web | Spring MVC (`spring-boot-starter-webmvc`) |
| Views | Thymeleaf + `thymeleaf-extras-springsecurity6` |
| Security | `spring-boot-starter-security` |

> **Note:** Spring Boot 4 split the old `spring-boot-starter-web` into `spring-boot-starter-webmvc`,
> and replaced the single `spring-boot-starter-test` with modular test starters
> (`-webmvc-test`, `-thymeleaf-test`, `-security-test`). That's reflected in `pom.xml`.

## Prerequisites

- **JDK 25** on your machine. This project will not build on older JDKs.
  Verify with `/usr/libexec/java_home -v 25` (macOS).
- No separate Maven install needed — use the bundled `./mvnw` wrapper.

If your shell defaults to an older JDK, point `JAVA_HOME` at 25 for the session:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 25)   # macOS
```

To make it the default in new terminals, append that line to `~/.zshrc`.

## Run

Development mode (auto-restarts on changes via `spring-boot-devtools`):

```bash
./mvnw spring-boot:run
```

Then open <http://localhost:8080> and log in with one of the demo accounts below.
Stop with `Ctrl-C`.

### Build a runnable jar

```bash
./mvnw clean package
java -jar target/spring-security-playground-0.0.1-SNAPSHOT.jar
```

Useful variants:

```bash
./mvnw clean package -DskipTests                                     # faster build
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090  # different port
```

## Demo accounts

Defined in-memory in `SecurityConfig` (BCrypt-hashed via a delegating encoder):

| Username | Password   | Roles            |
|----------|------------|------------------|
| `user`   | `password` | `USER`           |
| `admin`  | `password` | `USER`, `ADMIN`  |

## Routes

| Path       | Access                      | Notes                                        |
|------------|-----------------------------|----------------------------------------------|
| `/`        | Public                      | Home; content adapts to auth state           |
| `/public`  | Public                      | `permitAll()`                                |
| `/login`   | Public                      | Custom Thymeleaf login form                  |
| `/profile` | Any authenticated user      | Shows the current `Authentication` details   |
| `/admin`   | `ADMIN` role only           | Returns **403** for non-admins               |
| `/logout`  | POST                        | Clears the session, redirects to `/?logout`  |

## Project layout

```
src/main/java/io/juakali/security/
  SpringSecurityPlaygroundApplication.java   # entry point
  config/SecurityConfig.java                 # the file you'll mostly edit
  web/PageController.java                     # maps URLs -> view names

src/main/resources/
  templates/                                  # Thymeleaf views
    fragments.html                            # shared, auth-aware nav bar (th:fragment)
    home.html  login.html  public.html  profile.html  admin.html
  static/css/app.css                          # styling (theme via :root CSS variables)
  application.properties
```

## How the security is wired

- A single `SecurityFilterChain` bean (the modern lambda DSL — `WebSecurityConfigurerAdapter`
  was removed back in Spring Security 6) defines the route rules, form login, and logout.
- Authentication uses an `InMemoryUserDetailsManager`. Swap it for a
  `JdbcUserDetailsManager` or a custom `UserDetailsService` to experiment with persistence.
- Passwords use a **delegating** `PasswordEncoder`, so hashes are stored with a `{bcrypt}`
  prefix and the algorithm can be migrated later without breaking existing passwords.
- **CSRF** is enabled (Spring Security default). The login form's hidden `_csrf` field is
  injected automatically by Thymeleaf because the `<form>` uses `th:action`.
- Templates use the Spring Security Thymeleaf dialect (`sec:authorize`, `sec:authentication`)
  to show/hide UI and render role badges based on the logged-in user.

## Using the project in IntelliJ IDEA

Register the JDK and point the project at it:

1. `File → Project Structure` (`⌘ ;`) → **Platform Settings → SDKs → `+` → Add JDK** →
   select `/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home`.
2. **Project Settings → Project** → set **SDK** and **Language level** to **25**.
3. In **Edit Configurations…**, leave the run config's JRE as *Default (project SDK)*.

## Ideas for next experiments

- Method security: `@EnableMethodSecurity` + `@PreAuthorize` on service methods.
- Persisted users: `JdbcUserDetailsManager` with a datasource.
- OAuth2 / OIDC login (`spring-boot-starter-oauth2-client`).
- A `@WithMockUser` integration test using `spring-boot-starter-security-test`.
