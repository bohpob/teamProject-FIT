package cz.cvut.fit.sp.chipin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Postman",
                        url = "https://www.postman.com/chipin-sp2/workspace/chipin"
                ),
                description = "OpenApi documentation for the Chipin project",
                title = "OpenApi specification - Chipin",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "https://localhost:8080"
                ),
                @Server(
                        description = "Keycloak ENV",
                        url = "http://localhost:8081"
                )
        }
)
public class OpenApiConfiguration {
}
