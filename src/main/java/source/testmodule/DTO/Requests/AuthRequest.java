package source.testmodule.DTO.Requests;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequest implements Serializable {
    @Email(message = "Invalid email format")
    private String Email;
    private String password;
}