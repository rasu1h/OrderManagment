package source.testmodule.Infrastructure.Persitence.RepositoryAdapters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.OrderMapper;
import source.testmodule.Domain.Repository.OrderRepositoryPort;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaOrderRepository;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaProductRepository;
import source.testmodule.Presentation.DTO.OrderDTO;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaRepository;
    private final OrderMapper mapper;
    private final JpaProductRepository jpaProductRepository;


    @Override
    public List<OrderDTO> findFilteredOrders(OrderStatus status, Double minPrice, Double maxPrice) {
        return jpaRepository.findFilteredOrders(status, minPrice, maxPrice)
                .stream()
                .map(mapper::toDTO )
                .toList();
    }

    @Override
    public Order save(Order order) {
        return mapper.toDomain(jpaRepository.save(jpaRepository.findById(order.getId()).get()));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(Order order) {
        jpaRepository.delete(jpaRepository.findById(order.getId()).get());
    }
}
