package cl.duoc.guias.consumidor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("cloud")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // Endpoint público
                        .requestMatchers("/actuator/health").permitAll()

                        // Descarga: gestion o descarga
                        .requestMatchers(
                                HttpMethod.GET,
                                "/guias/*/download"
                        )
                        .access((authentication, context) -> {

                            var authResult = authentication.get();

                            boolean hasScope =
                                    authResult.getAuthorities()
                                            .stream()
                                            .anyMatch(authority ->
                                                authority.getAuthority()
                                                    .equals(
                                                        "SCOPE_guias.readwrite"
                                                    )
                                            );

                            boolean hasAllowedRole =
                                    authResult.getAuthorities()
                                            .stream()
                                            .anyMatch(authority ->
                                                authority.getAuthority()
                                                    .equals(
                                                        "ROLE_DESCARGA_GUIAS"
                                                    )
                                                ||
                                                authority.getAuthority()
                                                    .equals(
                                                        "ROLE_GESTION_GUIAS"
                                                    )
                                            );

                            return new AuthorizationDecision(
                                    hasScope && hasAllowedRole
                            );
                        })

                        // Resto de endpoints: solo gestion
                        .requestMatchers("/guias/**")
                        .access((authentication, context) -> {

                            var authResult = authentication.get();

                            boolean hasScope =
                                    authResult.getAuthorities()
                                            .stream()
                                            .anyMatch(authority ->
                                                authority.getAuthority()
                                                    .equals(
                                                        "SCOPE_guias.readwrite"
                                                    )
                                            );

                            boolean hasRoleGestion =
                                    authResult.getAuthorities()
                                            .stream()
                                            .anyMatch(authority ->
                                                authority.getAuthority()
                                                    .equals(
                                                        "ROLE_GESTION_GUIAS"
                                                    )
                                            );

                            return new AuthorizationDecision(
                                    hasScope && hasRoleGestion
                            );
                        })

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        new JwtRoleConverter()
                                )
                        )
                )
                .build();
    }
}