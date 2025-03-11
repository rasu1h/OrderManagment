package source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

import java.util.Optional;


public interface JpaUserRepository extends JpaRepository<UserJpaEntity, Long> {
    UserJpaEntity findByName(String username);
    Optional<UserJpaEntity> findByEmail(String email);
    UserJpaEntity findByNameOrEmail(String username, String email);
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);
}
