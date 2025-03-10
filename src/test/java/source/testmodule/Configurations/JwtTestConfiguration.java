//package source.testmodule.Configurations;
//
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
//import source.testmodule.Infrastructure.Configurations.WebConfig;
//import source.testmodule.Infrastructure.Repository.UserRepository;
//
//@TestConfiguration
//public class JwtTestConfiguration {
//
//    @Bean
//    public UserRepository userRepository() {
//        // Создаем мок для UserRepository
//        return Mockito.mock(UserRepository.class);    }
//
//    @Bean
//    public JwtTokenProvider jwtTokenProvider() {
//        // Здесь можно вернуть настоящую реализацию или мок, если логика не важна для тестов
//        return new JwtTokenProvider();
//    }
//
//    @Bean
//    public WebConfig webConfig(UserRepository userRepository) {
//        return new WebConfig(userRepository);
//    }
//}
