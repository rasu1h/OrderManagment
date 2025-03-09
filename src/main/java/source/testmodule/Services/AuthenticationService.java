package source.testmodule.Services;


import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import source.testmodule.DTO.Requests.AuthRequest;
import source.testmodule.DTO.Requests.SignUpRequest;
import source.testmodule.DTO.Responses.AuthResponse;

public interface AuthenticationService {
    AuthResponse authenticate(SignUpRequest authRequest);

}
