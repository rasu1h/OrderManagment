package source.testmodule.Infrastructure.Persitence.RepositoryAdapters;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.OrderMapper;
import source.testmodule.Domain.Repository.OrderRepositoryPort;
import source.testmodule.Infrastructure.Persitence.Mappers.ProductMapper;
import source.testmodule.Infrastructure.Persitence.Mappers.UserMapper;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaOrderRepository;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaProductRepository;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaUserRepository;
import source.testmodule.Presentation.DTO.OrderDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaRepository;
    private final JpaUserRepository jpaUserRepository;
    private final OrderMapper orderMapper;
    private final JpaProductRepository jpaProductRepository;


    @Override
    public List<OrderDTO> findFilteredOrders(OrderStatus status, Double minPrice, Double maxPrice) {
        return jpaRepository.findFilteredOrders(status, minPrice, maxPrice)
                .stream()
                .map(orderMapper::toDTO )
                .toList();
    }

    @Override
    public Order save(Order order) {
        // 1. Получаем продукт и пользователя с проверкой на null
        ProductJpaEntity product = jpaProductRepository.findById(order.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        UserJpaEntity user = jpaUserRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2. Создаем новую сущность
        OrderJpaEntity orderJpaEntity = new OrderJpaEntity();
        orderJpaEntity.setDescription(order.getDescription());
        orderJpaEntity.setQuantity(order.getQuantity());
        orderJpaEntity.setStatus(order.getStatus());

        // 3. Устанавливаем связанные сущности
        orderJpaEntity.setProductJpaEntity(product);
        orderJpaEntity.setUserJpaEntity(user);

        // 4. Расчет цены с проверкой данных
        if (product.getPrice() == null) {
            throw new IllegalStateException("Product price is not set");
        }


        orderJpaEntity.setPrice(order.getPrice());

        // 5. Сохранение и возврат результата
        OrderJpaEntity savedOrder = jpaRepository.save(orderJpaEntity);
        return orderMapper.toDomain(savedOrder);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(orderMapper::toDomain);
    }

    @Override
    public void delete(Order order) {
        jpaRepository.delete(jpaRepository.findById(order.getId()).get());
    }
}
