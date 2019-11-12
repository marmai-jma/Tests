package com.fedou.workshops.devtestingtour.exposition.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerDocumentationConfig {
    @Value("${info.app.name}")
    private String name;
    @Value("${info.app.description}")
    private String description;
    @Value("${info.app.version}")
    private String version;

    /**
     * Default construtor
     */
    public SwaggerDocumentationConfig() {
        super();

    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(name).description(description)
                //.license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("").version(version).contact(new Contact("", "", "someone@somewhere.com")).build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

}