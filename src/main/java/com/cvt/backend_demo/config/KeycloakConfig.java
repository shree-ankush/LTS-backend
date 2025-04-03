package com.cvt.backend_demo.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class KeycloakConfig {

    @Value("${keycloak.auth.server.url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.username}")
    private String keycloakUsername;

    @Value("${keycloak.password}")
    private String keycloakPassword;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(keycloakUsername)
                .password(keycloakPassword)
                .build();
    }
}
