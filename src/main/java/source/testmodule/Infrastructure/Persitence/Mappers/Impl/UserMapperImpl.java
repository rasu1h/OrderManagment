package source.testmodule.Infrastructure.Persitence.Mappers.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.UserMapper;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            throw  new IllegalArgumentException("UserJpaEntity is null");
        }
        User user = new User();
        user.setId(entity.getId());
        user.setRole(entity.getRole());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword("PROTECTED");
        user.setAuthorities(entity.getAuthorities().stream().toList());
        return user;
    }




}
