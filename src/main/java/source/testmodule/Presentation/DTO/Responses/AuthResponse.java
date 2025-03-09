package source.testmodule.Presentation.DTO.Responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Ответ с JWT токеном")
@AllArgsConstructor
public class AuthResponse {
    @Schema(description = "Сообщение для пользователя", example = "Успешная авторизация")
    private String message;

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}