package source.testmodule.Application.Services;

import jakarta.validation.Valid;
import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;

import java.util.List;


public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest, User currentUserJpaEntity);

    OrderDTO updateOrder(Long orderId, @Valid OrderRequest request, User currentUserJpaEntity);

    OrderDTO getOrderById(Long orderId, User currentUserJpaEntity);

    List<OrderDTO> getFilteredOrders(OrderStatus status, Double minPrice, Double maxPrice);

    public void softDelete(Long orderId);
}
