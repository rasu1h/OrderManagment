package source.testmodule.Domain.Repository;

import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

import java.util.Optional;

public interface UserRepositoryPort {
    User findByName(String username);
    Optional<User> findByEmail(String email);
    User findByNameOrEmail(String username, String email);
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);

    UserJpaEntity save(UserJpaEntity userJpaEntity);
}
