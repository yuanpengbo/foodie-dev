package org.yuan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

   //核心配置 docket  http://localhost:8080/swagger-ui/html 原路径

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("org.yuan.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("小明同学商城")
                .contact(new Contact(

                        "xiaoming",
                        "https://xiaomingtongx.com",
                        "xiaomingtongx@666.com"
                ))
                .description("小明同学的商城，爱生活，爱小明")
                .termsOfServiceUrl("https://xiaomingtongxue.com")
                .build();

    }
}
