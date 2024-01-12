package cz.cvut.fit.sp.chipin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
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
        }
)
@Configuration
public class OpenApiConfiguration {
}
