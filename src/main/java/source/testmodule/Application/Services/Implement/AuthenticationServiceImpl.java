package source.testmodule.Application.Services.Implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
import source.testmodule.Presentation.DTO.Responses.AuthResponse;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Domain.Enums.UserRole;
import source.testmodule.Infrastructure.Repository.UserRepository;
import source.testmodule.Application.Services.AuthenticationService;

/**
 * Implementation of the AuthenticationService interface.
 * Provides methods for user authentication and registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user based on the provided sign-up request.
     *
     * @param request the sign-up request containing user details
     * @return the authentication response containing a success message and JWT token
     * @throws IllegalArgumentException if the email is already in use or the password is empty
     */
    @Override
    @Transactional
    public AuthResponse authenticate(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is busy.", null);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is empty!", null);
        }

        return createUser(request);
    }

    /**
     * Creates a new user based on the provided sign-up request.
     *
     * @param request the sign-up request containing user details
     * @return the authentication response containing a success message and JWT token
     */
    private AuthResponse createUser(SignUpRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encode the password for security
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);
        log.info("User {} signed up successfully", user.getEmail());
        return new AuthResponse("User created successfully", jwtTokenProvider.generateToken(user));
    }
}