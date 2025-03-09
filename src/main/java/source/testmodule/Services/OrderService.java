package source.testmodule.Services;

import source.testmodule.DTO.OrderDTO;
import source.testmodule.DTO.Requests.OrderRequest;

public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest, Long userId);
}
