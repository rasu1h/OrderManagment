//package source.testmodule.ControllersTest;
//
//import jakarta.security.auth.message.AuthException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import source.testmodule.Application.Services.Exceptions.GlobalExceptionHandler;
//import source.testmodule.Domain.Enums.UserRole;
//import source.testmodule.Infrastructure.Configurations.Jwt.JwtAuthFilter;
//import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
//import source.testmodule.Infrastructure.Configurations.Security.SecurityConfig;
//import source.testmodule.Presentation.Controllers.AuthController;
//import source.testmodule.Presentation.DTO.Requests.AuthRequest;
//import source.testmodule.Presentation.DTO.Requests.SignUpRequest;
//import source.testmodule.Presentation.DTO.Responses.AuthResponse;
//import source.testmodule.Application.Services.AuthenticationService;
//import source.testmodule.Domain.Entity.User;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(AuthController.class)
//@Import({SecurityConfig.class, JwtTokenProvider.class})
//@ContextConfiguration(classes = AuthControllerTest.TestConfig.class)
//@TestPropertySource(properties = {
//        "spring.main.allow-bean-definition-overriding=true",
//        "spring.jpa.hibernate.ddl-auto=update",
//        "jwt.secret=test-secret-1234567890"
//})
//class AuthControllerTest {
//
//    @Configuration
//    static class TestConfig {
//        @Bean
//        @Primary
//        public JwtAuthFilter jwtAuthFilter() {
//            return mock(JwtAuthFilter.class);
//        }
//
//        @Bean
//        @Primary
//        public AuthenticationManager authenticationManager() {
//            return mock(AuthenticationManager.class);
//        }
//
//        @Bean
//        @Primary
//        public JwtTokenProvider jwtTokenProvider() {
//            JwtTokenProvider mockProvider = mock(JwtTokenProvider.class);
//            when(mockProvider.generateToken(any(UserDetails.class)))
//                    .thenReturn("mocked.jwt.token");
//            return mockProvider;
//        }
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private AuthenticationService authService; // Mock the service directly
//
//    @Test
//    void login_Success() throws Exception {
//        AuthResponse mockResponse = new AuthResponse("Success", "mocked.jwt.token");
//        when(authService.Authenticate(any(AuthRequest.class))).thenReturn(mockResponse);
//
//        MvcResult result = mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody = result.getResponse().getContentAsString();
//        System.out.println("Response Body: " + responseBody); // Вывод ответа
//
//        mockMvc.perform(asyncDispatch(result))
//                .andExpect(jsonPath("$.token").value("mocked.jwt.token"));
//    }
//
//    @Test
//    void login_InvalidCredentials() throws Exception {
//        when(authService.Authenticate(any(AuthRequest.class)))
//                .thenThrow(new BadCredentialsException("Invalid credentials"));
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"wrong@example.com\",\"password\":\"wrong\"}"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void register_Success() throws Exception {
//        AuthResponse mockResponse = new AuthResponse("User created", "token");
//        // Используйте register вместо Register
//        when(authService.Register(any(SignUpRequest.class)))
//                .thenReturn(mockResponse);
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"newUser\",\"email\":\"new@example.com\",\"password\":\"password\"}"))
//                .andExpect(status().isCreated()); // Теперь ожидается 201
//    }
//
//    @Test
//    void register_DuplicateEmail() throws Exception {
//        when(authService.Register(any(SignUpRequest.class)))
//                .thenThrow(new DataIntegrityViolationException("Email already in use"));
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"user\",\"email\":\"exists@example.com\",\"password\":\"pass\"}"))
//                .andExpect(status().isConflict());
//    }
//}