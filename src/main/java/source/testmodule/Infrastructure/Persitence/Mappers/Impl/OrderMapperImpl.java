package source.testmodule.Infrastructure.Persitence.Mappers.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.OrderMapper;
import source.testmodule.Infrastructure.Persitence.Mappers.ProductMapper;
import source.testmodule.Infrastructure.Persitence.Mappers.UserMapper;
import source.testmodule.Presentation.DTO.OrderDTO;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @Override
    public Order toDomain(OrderJpaEntity entity) {
        if (entity == null) {
           throw new IllegalArgumentException("Entity cannot be null or not found " + this);
        }
        Order order = new Order();

        order.setId(entity.getId());
        order.setDescription(entity.getDescription());
        order.setQuantity(entity.getQuantity());
        order.setPrice(entity.getPrice());
        order.setStatus(entity.getStatus());

        order.setUser(userMapper.toDomain(entity.getUserJpaEntity()));
        order.setProduct(productMapper.toDomain(entity.getProductJpaEntity()));
        return order;
    }


    @Override
    public OrderDTO toDTO(Order domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Domain cannot be null or not found " + this);
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(domain.getId());
        orderDTO.setDescription(domain.getDescription());
        orderDTO.setQuantity(domain.getQuantity());
        orderDTO.setStatus(domain.getStatus());
        orderDTO.setUsers(domain.getUser().getId() != null ? domain.getId() : null);
        orderDTO.setProducts(domain.getProduct().getId() != null ? domain.getId() : null);
         return orderDTO;
    }

    @Override
    public OrderDTO toDTO(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            throw new IllegalArgumentException("Entity cannot be null or not found " + this);
        }
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(orderJpaEntity.getId());
        orderDTO.setDescription(orderJpaEntity.getDescription());
        orderDTO.setQuantity(orderJpaEntity.getQuantity());
        orderDTO.setStatus(orderJpaEntity.getStatus());

        orderDTO.setUsers(userMapper.toDomain(orderJpaEntity.getUserJpaEntity()).getId());
        orderDTO.setProducts(productMapper.toDomain(orderJpaEntity.getProductJpaEntity()).getId());
         return orderDTO;
    }
}
