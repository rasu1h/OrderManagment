package source.testmodule.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.testmodule.Presentation.DTO.Requests.AuthRequest;
import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
import source.testmodule.Presentation.DTO.Responses.AuthResponse;
import source.testmodule.Application.Services.AuthenticationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "User Authentication and Registration")
public class AuthController {

    private final AuthenticationService authService;

    /**
     * Login endpoint.
     *
     * @param request the authentication request containing user credentials
     * @return a response with a message and JWT token for the authenticated user
     */
    @Operation(
            summary = "User Login",
            description = "Returns a JWT token for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.Authenticate(request));
    }

    /**
     * Register endpoint.
     *
     * @param request the sign-up request containing user details
     * @return a response with a message and JWT token for the newly registered user
     */
    @Operation(
            summary = "Register New User",
            description = "Creates a new user and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.Register(request));
    }
}
