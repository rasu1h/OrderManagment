package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.Email;
import lombok.NoArgsConstructor;

/**
 * DTO class for the authentication request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SignUpRequest", description = "Request for signing up")
public class SignUpRequest {
    @Email(message = "Invalid email format")
    @Schema(description = "User email", example = "example@example.com")
    private String Email;
    private String username;
    private String password;


}
