package source.testmodule.Presentation.DTO.Responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO class for the authentication response.
 */
@Data
@Schema(description = "Authentication response")
@AllArgsConstructor
public class AuthResponse implements Serializable {
    @Schema(description = "Message for user", example = "Successfully logged in")
    private String message;

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}