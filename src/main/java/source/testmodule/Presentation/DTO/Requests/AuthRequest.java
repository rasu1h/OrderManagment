package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO class for the authentication request.
 */

@Data
@Schema(description = "Request body for authentication")
@AllArgsConstructor
public class AuthRequest {
    @Schema(description = "Email users", example = "user@example.com")
    @Email
    private String email;

    @Schema(description = "Password", example = "mySecurePassword123")
    private String password;
}