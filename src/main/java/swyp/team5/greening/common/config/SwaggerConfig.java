package swyp.team5.greening.common.config;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8000/api-docs
@OpenAPIDefinition(
        info = @Info(
                title = "Greening API",
                description = "Master AccessToken = eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3MjQzMjcxLCJleHAiOjE4MzM2NDMyNzF9.bdX-_o8SBbnJnhsUPo8UoogXPrOw1jABrRjf71qqFCI",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String accessToken = "ACCESS_TOKEN";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(accessToken);

        Components components = new Components()
                .addSecuritySchemes(accessToken,
                        new SecurityScheme()
                                .name(accessToken)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(AUTHORIZATION));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
