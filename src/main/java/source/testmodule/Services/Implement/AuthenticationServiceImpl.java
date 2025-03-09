package source.testmodule.Services.Implement;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import source.testmodule.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.DTO.Requests.AuthRequest;
import source.testmodule.DTO.Requests.SignUpRequest;
import source.testmodule.DTO.Responses.AuthResponse;
import source.testmodule.DataBase.Entity.User;
import source.testmodule.DataBase.Enums.UserRole;
import source.testmodule.DataBase.Repository.UserRepository;
import source.testmodule.Services.AuthenticationService;

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
