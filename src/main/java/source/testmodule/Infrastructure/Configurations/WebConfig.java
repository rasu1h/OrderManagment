package source.testmodule.Infrastructure.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Configurations.Security.CurrentUserArgumentResolver;
import source.testmodule.Infrastructure.Repository.UserRepository;

import java.util.List;

/**
 * Web configuration class for setting up custom argument resolvers, formatters, and resource handlers.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    /**
     * Adds custom argument resolvers to the list of resolvers.
     *
     * @param resolvers the list of argument resolvers to add to
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver(userRepository));
    }

    /**
     * Adds custom formatters to the registry.
     *
     * @param registry the formatter registry to add to
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToOrderStatusConverter());
    }

    /**
     * Converter class to convert String to OrderStatus enum.
     */
    public static class StringToOrderStatusConverter implements Converter<String, OrderStatus> {
        /**
         * Converts a String to an OrderStatus enum.
         *
         * @param source the source string to convert
         * @return the converted OrderStatus enum
         */
        @Override
        public OrderStatus convert(String source) {
            return OrderStatus.valueOf(source.toUpperCase());
        }
    }
}