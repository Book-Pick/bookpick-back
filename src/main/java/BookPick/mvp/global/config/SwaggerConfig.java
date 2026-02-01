package BookPick.mvp.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT 토큰을 입력하세요 (Bearer 접두사 불필요)")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .info(apiInfo())
                // 태그별로 그룹화
                .addTagsItem(new Tag().name("Reading Preference").description("유저 독서 취향 관련 API"))
                .addTagsItem(new Tag().name("Curation").description("큐레이션 관련 API"))
                .addTagsItem(new Tag().name("Book Search").description("책 검색 관련 API"))
                .addTagsItem(new Tag().name("Auth").description("인증 관련 API"))
                .addTagsItem(new Tag().name("User").description("유저 관련 API"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Swagger Book Pick ")
                .description("블라인드 큐레이션 스토어 API 및 스키마")
                .version("1.0.0");
    }
}
