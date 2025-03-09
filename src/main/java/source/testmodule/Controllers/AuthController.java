package source.testmodule.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import source.testmodule.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.DTO.Requests.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.testmodule.DTO.Requests.SignUpRequest;
import source.testmodule.DTO.Responses.AuthResponse;
import source.testmodule.DataBase.Entity.User;
import source.testmodule.Services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        log.info("User {} signed in", request.getEmail());

        String jwt = jwtTokenProvider.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponse("Signed in completed succesfully", jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
