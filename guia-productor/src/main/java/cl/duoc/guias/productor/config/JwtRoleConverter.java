package cl.duoc.guias.productor.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>(scopeConverter.convert(jwt));
        addClaim(authorities, jwt.getClaim("extension_guiaRole"));
        addClaim(authorities, jwt.getClaim("roles"));
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    private void addClaim(List<GrantedAuthority> authorities, Object claim) {
        if (claim instanceof String value) {
            addRole(authorities, value);
        } else if (claim instanceof Collection<?> values) {
            values.forEach(value -> addRole(authorities, String.valueOf(value)));
        }
    }

    private void addRole(List<GrantedAuthority> authorities, String value) {
        String normalized = value.trim().toLowerCase();
        String role = switch (normalized) {
            case "descarga", "descargas", "download", "descargar", "solo_descarga" -> "ROLE_DESCARGA_GUIAS";
            case "gestion", "gestor", "admin", "administrador" -> "ROLE_GESTION_GUIAS";
            default -> "ROLE_" + normalized.toUpperCase().replace('-', '_');
        };
        authorities.add(new SimpleGrantedAuthority(role));
    }
}
