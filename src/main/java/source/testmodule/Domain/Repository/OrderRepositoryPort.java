package source.testmodule.Domain.Repository;

// source/testmodule/domain/repositories/OrderRepositoryPort.java

import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Presentation.DTO.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    List<OrderDTO> findFilteredOrders(
            OrderStatus status,
            Double minPrice,
            Double maxPrice
    );

    Order save(Order order);
    Optional<Order> findById(Long id);
    void delete(Order order);
}