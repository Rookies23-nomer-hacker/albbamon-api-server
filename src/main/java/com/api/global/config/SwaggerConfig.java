package com.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    private final static String SWAGGER_TITLE = "Albbamon API";
    private final static String SWAGGER_DESC = "[SK 쉴더스 루키즈 23기] 모의해킹 최종 프로젝트 API";
    private final static String SWAGGER_VERSION = "1.0.0";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(SWAGGER_TITLE)
                .description(SWAGGER_DESC)
                .version(SWAGGER_VERSION);
    }
}
