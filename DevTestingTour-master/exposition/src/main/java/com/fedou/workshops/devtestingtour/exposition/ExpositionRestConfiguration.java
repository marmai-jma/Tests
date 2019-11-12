package com.fedou.workshops.devtestingtour.exposition;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootConfiguration
@EnableWebMvc
@EnableSwagger2
@EnableWebSecurity
@ComponentScan
public class ExpositionRestConfiguration {
}
