package source.testmodule.Application.Services.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaUserRepository;
import source.testmodule.Application.Services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final JpaUserRepository jpaUserRepository;

    /**
     * Load user by username
     * for authentication and authorization
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJpaEntity userJpaEntity = jpaUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new org.springframework.security.core.userdetails.User(
                userJpaEntity.getEmail(),
                userJpaEntity.getPassword(),
                userJpaEntity.getAuthorities()
        );
    }

    /**
     * Get user by id
     * @param userId
     * @return
     */
    @Override
    public UserJpaEntity getUserById(Long userId) {
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}