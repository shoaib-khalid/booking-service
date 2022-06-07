package com.kalsym.service.vertical.config;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hasan
 */
import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * @author kalsym
 */

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket ServiceVerticalApi() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                //.forCodeGeneration(true)
                //.genericModelSubstitutes(ResponseEntity.class)
                //.ignoredParameterTypes(Pageable.class)
                //.ignoredParameterTypes(java.sql.Date.class)
                //.directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                //.directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                //.directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(new ApiKey("Bearer", AUTHORIZATION_HEADER, "header")))
                .useDefaultResponseMessages(false);
        docket = docket.select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
        return docket;

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("service-vertical")
                .description("Used to manage resource reservations")
                .termsOfServiceUrl("not added yet")
                .license("Kalsym Systems")
                .licenseUrl("").version("1.0").build();
    }

    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("Bearer", authorizationScopes));
    }
}

