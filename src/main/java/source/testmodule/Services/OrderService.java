package source.testmodule.Services;

import source.testmodule.DTO.OrderDTO;
import source.testmodule.DTO.Requests.OrderRequest;
import source.testmodule.DataBase.Entity.User;

public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest, User currentUser);
}
