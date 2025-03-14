package source.testmodule.Infrastructure.Configurations.Security;

import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import source.testmodule.Domain.Model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import source.testmodule.Domain.Repository.UserRepositoryPort;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

/**
 * Argument resolver to inject the current authenticated user into controller methods.
 * @see CurrentUser annotation
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepositoryPort userRepositoryPort;

    /**
     * Constructor to initialize the UserRepository.
     *
     * @param userRepositoryPort the user repository to use for fetching user details
     */
    public CurrentUserArgumentResolver(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Checks if the parameter is annotated with @CurrentUser and is of type User.
     *
     * @param parameter the method parameter to check
     * @return true if the parameter is supported, false otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && User.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     * Resolves the argument by fetching the current authenticated user from the security context.
     *
     * @param parameter the method parameter
     * @param mavContainer the ModelAndViewContainer
     * @param webRequest the web request
     * @param binderFactory the data binder factory
     * @return the current authenticated user
     * @throws AccessDeniedException if the user is not authenticated
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        String email = authentication.getName();
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        if (user.getId() == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }
}