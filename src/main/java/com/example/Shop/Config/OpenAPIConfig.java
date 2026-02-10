import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    // 1. Cấu hình cá nhân hóa tiêu đề (Dùng chung cho toàn bộ Project)
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DỰ ÁN SHOP 2026 - ADMIN & USER") // Thay đổi dòng chữ bạn muốn ở đây
                        .version("1.0.0")
                        .description("Hệ thống quản lý bán hàng và tài liệu API cá nhân hóa"));
    }

    // 2. Cấu hình Nhóm để đảm bảo đọc đủ API
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("shop-all") // Tên nhóm hiển thị trong dropdown Swagger
                .pathsToMatch("/**") // Quét toàn bộ các đường dẫn trong project
                .build();
    }
}