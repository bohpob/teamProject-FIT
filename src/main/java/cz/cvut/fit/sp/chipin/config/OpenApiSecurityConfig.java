package cz.cvut.fit.sp.chipin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Chipin REST API",
                description = "OpenApi documentation for the Chipin project",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Host ENV",
                        url = "https://chipin.ninja"
                ),
                @Server(
                        description = "Local ENV",
                        url = "https://localhost:8080"
                )
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "OAuth Chipin Authentication"
                )
        }
)
@Configuration
public class OpenApiSecurityConfig {

        @Value("${keycloak.auth-server-url}")
        private String authServerUrl;
        @Value("${keycloak.realm}")
        private String realm;

        private static final String OAUTH_SCHEME_NAME = "OAuth Chipin Authentication";

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                        .components(new Components()
                                .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                        .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                        .info(new io.swagger.v3.oas.models.info.Info().title("Chipin App")
                                .description("Chipin App.")
                                .version("1.0"));
        }

        private SecurityScheme createOAuthScheme() {
                return new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                                .authorizationCode(createAuthorizationCodeFlow()));
        }

        private OAuthFlow createAuthorizationCodeFlow() {
                return new OAuthFlow()
                        .authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth")
                        .tokenUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token");
        }
}
