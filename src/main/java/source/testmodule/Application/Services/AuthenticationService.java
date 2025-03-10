package source.testmodule.Application.Services;


import jakarta.validation.Valid;
import source.testmodule.Presentation.DTO.Requests.AuthRequest;
import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
import source.testmodule.Presentation.DTO.Responses.AuthResponse;

/**
 * Interface for the authentication service.
 */
public interface AuthenticationService {
    AuthResponse Register(SignUpRequest authRequest);

    AuthResponse Authenticate(@Valid AuthRequest request);
}
