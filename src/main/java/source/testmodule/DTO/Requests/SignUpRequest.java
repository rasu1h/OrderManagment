package source.testmodule.DTO.Requests;

import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class SignUpRequest {
    @Email(message = "Invalid email format")
    private String Email;
    private String username;
    private String password;
}
