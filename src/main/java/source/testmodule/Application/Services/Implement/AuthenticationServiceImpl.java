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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse authenticate(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email is busy.", null);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return new AuthResponse("Password is empty!", null);
        }

        return createUser(request);
    }

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
