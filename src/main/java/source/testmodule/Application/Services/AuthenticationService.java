package source.testmodule.Application.Services;


import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
import source.testmodule.Presentation.DTO.Responses.AuthResponse;

public interface AuthenticationService {
    AuthResponse authenticate(SignUpRequest authRequest);
}
