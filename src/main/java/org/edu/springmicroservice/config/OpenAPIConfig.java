package org.edu.springmicroservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${project.name}")
    private String name;

    @Value("${project.description}")
    private String description;

    @Value("${project.version}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(name)
                        .description(description)
                        .contact(getContact()).version(version));
    }

    private Contact getContact() {
        final Contact contact = new Contact();
        contact.setEmail("dev.jefster@gmail.com");
        contact.setName("Jefster Farlei");
        return contact;
    }
}
