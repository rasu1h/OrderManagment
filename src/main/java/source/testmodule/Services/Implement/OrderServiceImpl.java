package source.testmodule.Services.Implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import source.testmodule.DTO.OrderDTO;
import source.testmodule.DTO.Requests.OrderRequest;
import source.testmodule.DataBase.Entity.Order;
import source.testmodule.DataBase.Entity.Product;
import source.testmodule.DataBase.Repository.OrderRepository;
import source.testmodule.DataBase.Repository.ProductRepository;
import source.testmodule.Services.OrderService;
import source.testmodule.Services.UserService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest, Long userId) {
        Order order = new Order();
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < orderRequest.getQuantity()) {
            throw new RuntimeException("Not enough quantity");
        }
        order.setDescription(orderRequest.getDescription());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(product.getPrice() * orderRequest.getQuantity());
        order.setProduct(product);
        order.setUser(userService.getUserById(userId));
        product.setQuantity(product.getQuantity() - orderRequest.getQuantity());

        orderRepository.save(order);
        productRepository.save(product);
        return  OrderDTO.fromEntity(order);
    }

}
