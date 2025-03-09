package source.testmodule.Application.Services;

import jakarta.validation.Valid;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.User;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest, User currentUser);

    OrderDTO updateOrder(Long orderId, @Valid OrderRequest request);

    OrderDTO getOrderById(Long orderId);
}
