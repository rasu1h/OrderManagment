package source.testmodule.DTO.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.Email;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @Email(message = "Invalid email format")
    private String Email;
    private String username;
    private String password;


}
