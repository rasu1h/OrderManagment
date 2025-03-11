package source.testmodule.Infrastructure.Persitence.Mappers;

import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

public interface UserMapper {
    User toDomain(UserJpaEntity entity);
}
