package cl.duoc.guias.productor.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("cloud")
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${azure.b2c.audience}")
    private String audience;

    @Value("${azure.b2c.role-claim:extension_guiaRole}")
    private String roleClaim;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST, "/guias")
                .access((authentication, context) -> {
                    boolean autorizado = authentication.get()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(java.util.stream.Collectors.toSet())
                        .containsAll(
                            java.util.Set.of(
                                "SCOPE_guias.readwrite",
                                "ROLE_GESTION_GUIAS"
                            )
                        );

                    return new AuthorizationDecision(autorizado);
                })
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth ->
                oauth.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }

    @Bean
    public NimbusJwtDecoder jwtDecoder() {

        NimbusJwtDecoder decoder =
            NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        var issuerValidator =
            JwtValidators.createDefaultWithIssuer(issuerUri);

        var audienceValidator =
            new AudienceValidator(audience);

        decoder.setJwtValidator(
            new DelegatingOAuth2TokenValidator<>(
                issuerValidator,
                audienceValidator
            )
        );

        return decoder;
    }

    private Converter<Jwt, AbstractAuthenticationToken>
        jwtAuthenticationConverter() {

        return jwt -> {
            Collection<GrantedAuthority> authorities =
                new ArrayList<>();

            String scope = jwt.getClaimAsString("scp");

            if (scope != null && !scope.isBlank()) {
                for (String value : scope.split(" ")) {
                    authorities.add(
                        new SimpleGrantedAuthority(
                            "SCOPE_" + value
                        )
                    );
                }
            }

            Object roleValue = jwt.getClaim(roleClaim);

            if (roleValue instanceof String role) {
                String normalized =
                    role.trim().toLowerCase();

                if ("gestion".equals(normalized)) {
                    authorities.add(
                        new SimpleGrantedAuthority(
                            "ROLE_GESTION_GUIAS"
                        )
                    );
                }

                if ("descarga".equals(normalized)
                        || "descargas".equals(normalized)) {
                    authorities.add(
                        new SimpleGrantedAuthority(
                            "ROLE_DESCARGA_GUIAS"
                        )
                    );
                }
            }

            return new JwtAuthenticationToken(
                jwt,
                authorities
            );
        };
    }
}