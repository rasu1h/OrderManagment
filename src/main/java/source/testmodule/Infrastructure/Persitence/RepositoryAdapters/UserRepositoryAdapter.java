package source.testmodule.Infrastructure.Persitence.RepositoryAdapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Model.User;
import source.testmodule.Domain.Repository.UserRepositoryPort;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.UserMapper;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaUserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final JpaUserRepository userRepository;
    private final UserMapper userMapper;



    @Override
    public User findByName(String username) {
        return userMapper.toDomain(userRepository.findByName(username)) ;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public User findByNameOrEmail(String username, String email) {
        return userMapper.toDomain(userRepository.findByNameOrEmail(username, email)) ;
    }

    @Override
    public Boolean existsByName(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserJpaEntity save(UserJpaEntity userJpaEntity) {
        return userRepository.save(userJpaEntity);
    }


}
