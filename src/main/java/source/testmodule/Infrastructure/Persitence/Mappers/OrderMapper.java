package source.testmodule.Infrastructure.Persitence.Mappers;

import source.testmodule.Domain.Model.Order;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Presentation.DTO.OrderDTO;

public interface OrderMapper {

    Order toDomain(OrderJpaEntity entity);
    OrderDTO toDTO(Order domain);
    OrderDTO toDTO(OrderJpaEntity orderJpaEntity);
    }
