package source.testmodule.Infrastructure.Persitence.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Model.Product;
import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Presentation.DTO.ProductDTO;

public interface UserMapper {
    User toDomain(UserJpaEntity entity);
}
