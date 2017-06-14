package com.eat.configs.web;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket controllersB2BApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("B2B")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.b2b"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket controllersB2CApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("B2C")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.b2c"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket controllersCommonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Common")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.common"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket controllersFoursquareApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Foursquare")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.foursquare"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket controllersRecommenderApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Recommender")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.recommender"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket controllersDemoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Demo")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eat.controllers.demo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EAT! API documentation")
                .description("EAT! API documentation")
                .termsOfServiceUrl("http://smetner.com")
                .contact(apiContact())
                .license("")
                .licenseUrl("")
                .version("1 alpha")
                .build();
    }

    private Contact apiContact() {
        return new Contact("Orlov Vladislav", "", "orlovv@digicode.net");
    }

}