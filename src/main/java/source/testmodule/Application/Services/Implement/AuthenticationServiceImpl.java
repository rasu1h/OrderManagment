package source.testmodule.Application.Services.Implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.Presentation.DTO.Requests.AuthRequest;
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
    private final AuthenticationManager authenticationManager;
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
    public AuthResponse Register(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException("Email is already in use!");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is empty!", null);
        }

        return createUser(request);
    }

    @Override
    public AuthResponse Authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if (authentication == null) {
            return ResponseEntity.badRequest().body(new AuthResponse("Invalid email or password", null)).getBody();
        }

        log.info("User {} signed in", request.getEmail());


        String jwt = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        log.info("User {} signed in", ((UserDetails) authentication.getPrincipal()));
        return new AuthResponse("User signed in successfully", jwt);
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