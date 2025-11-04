### Repository guidance for AI coding agents

This Spring Boot web application (Java 17, Spring Boot 3.x) uses Thymeleaf templates, MyBatis mappers, and Spring Security. Use these notes to make safe, useful edits and code suggestions.

- Big picture
  - Entry point: `src/main/java/com/soldesk/festival/FestivalApplication.java` (standard Spring Boot run).
  - Web layer: Controllers in `src/main/java/com/soldesk/festival/controller/` (e.g. `HomeContoroller`, `MemberRestController`, `BoardContoroller`). Controllers return Thymeleaf view names or JSON for REST endpoints.
  - Service layer: `src/main/java/com/soldesk/festival/service/` (e.g. `MemberService`, `AuthService`). Business logic, transactions, and security-aware operations live here.
  - Data layer: MyBatis mappers in `src/main/java/com/soldesk/festival/mapper/` (e.g. `MemberMapper`, `CompanyMapper`) with SQL annotated on interfaces. Type handlers live in `src/main/java/com/soldesk/festival/handler/` and are registered via `mybatis.type-handlers-package` in `application.properties`.
  - Templates & static assets: Thymeleaf templates under `src/main/resources/templates/` and static files under `src/main/resources/static/`.

- Important architectural patterns and conventions
  - MyBatis annotations: many mappers use inline `@Select/@Insert/@Update/@Delete` annotations. Preserve SQL parameter names and type handlers (e.g. `MemberRoleTypeHandler`, `CompanyRoleTypeHandler`). When adding new mapper methods, follow the existing annotation style and include `@Results/@Result` mappings when returning DTOs.
  - DTOs vs domain objects: DTO classes live in `src/main/java/com/soldesk/festival/dto/`. Controllers and services exchange DTOs (e.g. `MemberDTO`, `MemberJoinDTO`, `MemberUpdateDTO`, `SecurityAllUsersDTO`). Follow existing builders and Lombok usage.
  - Security: `src/main/java/com/soldesk/festival/config/SecurityConfig.java` uses role-based access via `MemberRole` and `CompanyRole` enums. Public endpoints (login/join) are explicitly permitted in the security filter chain. Any API added under `/api/**` should consider CSRF exclusions and role checks in `SecurityConfig`.
  - Authentication: `AuthService` implements `UserDetailsService` and supports both `Member` and `Company` users by returning `SecurityAllUsersDTO`. When extending authentication, keep compatibility with `SecurityAllUsersDTO` and `AuthenticationManager` usage.
  - Password handling: `AuthUtil` (used from services) performs encoding and verification. New code must use the `PasswordEncoder` bean (BCrypt) provided in `SecurityConfig` or `AuthUtil` to store/compare passwords.

- Build, run, and test notes (project-specific)
  - Build/run with Gradle wrapper from repo root: `./gradlew bootRun` or `./gradlew build` (Java 17 toolchain is configured in `build.gradle`).
  - The project uses MySQL by default. Connection settings are in `src/main/resources/application.properties`. The file currently points to an Azure MySQL instance and contains credentials; treat these as sensitive — do not leak or change them without coordination. For local development, replace `spring.datasource.url`, `username` and `password` or use Spring profiles.
  - MyBatis: type handlers are configured via `mybatis.type-handlers-package=com.soldesk.festival.handler`. When adding enums that map to DB columns, add a corresponding BaseTypeHandler and register it in that package.
  - Tests: standard JUnit 5 tests live under `src/test/java/`. Run tests with `./gradlew test`.

- Project-specific code patterns and gotchas
  - Mapper SQL strings: Several mapper methods currently have syntax issues (missing braces or typos) — review any mapper changes carefully and prefer using XML mapper files for complex SQL if needed. Example: `MemberMapper.insertMember` contains broken parameter placeholders. When editing mapper SQL, run unit tests or integration tests to catch SQL/string errors.
  - Role enums: `src/main/java/com/soldesk/festival/config/MemberRole.java` and `CompanyRole.java` include helper methods (e.g. `fromString`, `hasHigherLevelThan`) — use these helpers rather than doing raw string comparisons.
  - Exception handling: Controllers often use `@ExceptionHandler(UserException.class)` to translate domain errors into HTTP responses. When adding service errors, prefer throwing `UserException` (or existing exceptions) to reuse handlers.
  - Thymeleaf view names: Controller methods typically return view names (e.g. `return "index";`) that map to `templates/`. When changing a view name, ensure the corresponding template exists and static assets referenced in `templates/common/` are preserved.

- Integration points & external dependencies
  - DB: MySQL (remote Azure instance by default). Mapper interfaces assume a schema/table layout (`member`, `company` etc.). Adding fields requires mapper and DTO updates.
  - OAuth: `spring-boot-starter-oauth2-client` is included — see `CustomOAuth2UserService` under `service/` for how OAuth attributes are mapped to internal DTOs.
  - JWT: `io.jsonwebtoken:jjwt` is present in `build.gradle` — if adding token-based auth flows, follow the existing token usage (search repository for JWT references).

- Examples to cite when making edits
  - Authentication flow: `AuthService.login()`, `SecurityConfig.formLogin(...)`, and `SecurityAllUsersDTO` are the trio to review when changing login behavior.
  - Mapper/type-handler pattern: `MemberMapper` + `handler/MemberRoleTypeHandler.java` + `application.properties` (`mybatis.type-handlers-package`) show how enums are mapped to DB columns.
  - User lifecycle: `MemberService.join()`, `MemberService.modifyMember()`, `MemberService.deleteMember()` — follow transactional boundaries (`@Transactional`) and `AuthUtil` password handling.

- Safety and style for AI edits
  - Preserve SQL and security-related code. Any change touching authentication, authorization, or SQL must include unit or integration test updates and be annotated in the PR description.
  - When adding new endpoints under `/api/**`, update `SecurityConfig` rules and CSRF exclusions as appropriate.
  - Avoid committing credentials. If you need to change database configuration for local runs, use Spring profiles and `application-local.properties` (create if needed) and update `.gitignore` accordingly.

If anything above is unclear or you'd like the file to emphasize other areas (frontend templates, specific controllers, or test patterns), tell me which parts to expand and I'll iterate.
