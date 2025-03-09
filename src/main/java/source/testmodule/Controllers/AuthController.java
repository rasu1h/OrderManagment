package source.testmodule.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import source.testmodule.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.Configurations.Security.CurrentUser;
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
@Schema(description = "Auth Controller")
public class AuthController {

    private final AuthenticationService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Login endpoint
     * @param request
     * @return message and jwt token for the user
     */

    @Operation(
            summary = "Авторизация пользователя",
            description = "Возвращает JWT токен для аутентифицированного пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная авторизация",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if (authentication == null) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

        log.info("User {} signed in", request.getEmail());


        String jwt = jwtTokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        log.info("User {} signed in", ((UserDetails) authentication.getPrincipal()));
        return ResponseEntity.ok(new AuthResponse("Signed in completed succesfully", jwt));
    }

    /**
     * Register endpoint
     * @param request
     * @return message and jwt token for the user
     */

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя и возвращает JWT токен"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная регистрация",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или email занят")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
