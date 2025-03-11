package source.testmodule.Infrastructure.Persitence.Mappers;

// source/testmodule/infrastructure/persistence/mappers/OrderMapper.java

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Presentation.DTO.OrderDTO;

public interface OrderMapper {

    Order toDomain(OrderJpaEntity entity);
    OrderDTO toDTO(Order domain);
    OrderDTO toDTO(OrderJpaEntity orderJpaEntity);
    }
