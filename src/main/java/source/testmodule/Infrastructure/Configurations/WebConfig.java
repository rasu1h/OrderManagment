package source.testmodule.Infrastructure.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Configurations.Security.CurrentUserArgumentResolver;
import source.testmodule.Infrastructure.Repository.UserRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig  implements WebMvcConfigurer {

    private final UserRepository userRepository;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver(userRepository));
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToOrderStatusConverter());
    }

    public static class StringToOrderStatusConverter implements Converter<String, OrderStatus> {
        @Override
        public OrderStatus convert(String source) {
            return OrderStatus.valueOf(source.toUpperCase());
        }
    }
}