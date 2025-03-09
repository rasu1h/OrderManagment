package source.testmodule.Application.Services.Implement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.Order;
import source.testmodule.Domain.Entity.Product;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Infrastructure.Repository.OrderRepository;
import source.testmodule.Infrastructure.Repository.ProductRepository;
import source.testmodule.Application.Services.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"orders", "userOrders"}, allEntries = true)
    public OrderDTO createOrder(OrderRequest orderRequest, User currentUser) {
        Order order = new Order();
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < orderRequest.getQuantity()) {
            throw new RuntimeException("Not enough quantity");
        }
        order.setDescription(orderRequest.getDescription());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(product.getPrice() * orderRequest.getQuantity());
        order.setProduct(product);
        order.setUser(currentUser);
        product.setQuantity(product.getQuantity() - orderRequest.getQuantity());

        orderRepository.save(order);
        productRepository.save(product);
        return  OrderDTO.fromEntity(order);
    }





    @CacheEvict(value = {"orders", "userOrders"}, key = "#orderId")
    public OrderDTO updateOrder(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setQuantity(request.getQuantity());
        order.setDescription(request.getDescription());
        order.setStatus(request.getStatus());
        order.setPrice(order.getProduct().getPrice() * request.getQuantity());
        return OrderDTO.fromEntity(orderRepository.save(order));
    }

    @Override
    @Cacheable(value = {"orders", "userOrders"}, key = "#orderId")
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return OrderDTO.fromEntity(order);
    }



}
