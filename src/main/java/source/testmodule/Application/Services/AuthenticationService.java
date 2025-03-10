package source.testmodule.Application.Services;


import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
import source.testmodule.Presentation.DTO.Responses.AuthResponse;

/**
 * Interface for the authentication service.
 */
public interface AuthenticationService {
    AuthResponse authenticate(SignUpRequest authRequest);
}
