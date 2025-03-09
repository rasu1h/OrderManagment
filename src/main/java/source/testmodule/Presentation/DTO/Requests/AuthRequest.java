package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Запрос авторизации")
@AllArgsConstructor
public class AuthRequest {
    @Schema(description = "Email пользователя", example = "user@example.com")
    @Email
    private String email;

    @Schema(description = "Пароль", example = "mySecurePassword123")
    private String password;
}