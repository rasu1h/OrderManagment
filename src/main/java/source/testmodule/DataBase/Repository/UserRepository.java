package source.testmodule.DataBase.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.DataBase.Entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);
    Optional<User> findByEmail(String email);
    User findByNameOrEmail(String username, String email);
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);
}
