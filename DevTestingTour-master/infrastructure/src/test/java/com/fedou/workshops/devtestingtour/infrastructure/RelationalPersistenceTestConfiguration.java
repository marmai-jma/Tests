package com.fedou.workshops.devtestingtour.infrastructure;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@PropertySource("application-test.properties")
public class RelationalPersistenceTestConfiguration extends RelationalPersistenceConfiguration {
}
