package source.testmodule.Application.Services;

import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

public interface UserService {
    UserJpaEntity getUserById(Long userId);
}
