package com.cvt.backend_demo.auth;



import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // ✅ Ensure Spring detects this class
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);

        List<GrantedAuthority> roleAuthorities = extractRoles(jwt).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        authorities.addAll(roleAuthorities);
        return authorities;
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

        if (resourceAccess == null || !resourceAccess.containsKey("leave-portal")) {
            return List.of(); // Return an empty list if no roles found
        }

        Map<String, Object> myApp = (Map<String, Object>) resourceAccess.get("leave-portal");

        if (myApp == null || !myApp.containsKey("roles")) {
            return List.of();
        }

        return (List<String>) myApp.get("roles");
    }



}