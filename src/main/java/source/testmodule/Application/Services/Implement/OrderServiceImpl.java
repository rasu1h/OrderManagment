package source.testmodule.Application.Services.Implement;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.Order;
import source.testmodule.Domain.Entity.Product;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Infrastructure.Repository.OrderRepository;
import source.testmodule.Infrastructure.Repository.ProductRepository;
import source.testmodule.Application.Services.OrderService;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the OrderService interface.
 * Provides methods for managing orders.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ordersCache")
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CacheManager cacheManager;

    /**
     * Checks if the order belongs to the current user.
     *
     * @param orderId the ID of the order
     * @param currentUser the current user
     * @return true if the order does not belong to the user, false otherwise
     * @throws EntityNotFoundException if the order is not found
     */
    boolean isUserOrder(Long orderId, User currentUser) {
        return orderRepository.findById(orderId)
                .map(order -> !Objects.equals(order.getUser().getId(), currentUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    /**
     * Creates a new order.
     *
     * @param orderRequest the order request containing order details
     * @param currentUser the current user making the request
     * @return the created order
     * @throws RuntimeException if the product is not found or there is not enough quantity
     * @throws IllegalArgumentException if the quantity is less than or equal to 0 or the description is too long
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'user_' + #currentUser.id"),
            @CacheEvict(key = "'filtered'"),
            @CacheEvict(key = "#result.id", condition = "#result != null")
    })
    public OrderDTO createOrder(OrderRequest orderRequest, User currentUser) {
        Order order = new Order();
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < orderRequest.getQuantity()) {
            throw new RuntimeException("Not enough quantity");
        }
        if (orderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (orderRequest.getDescription().length() > 255) {
            throw new IllegalArgumentException("Description is too long");
        }
        order.setDescription(orderRequest.getDescription());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(product.getPrice() * orderRequest.getQuantity());
        order.setProduct(product);
        order.setUser(currentUser);
        product.setQuantity(product.getQuantity() - orderRequest.getQuantity());

        orderRepository.save(order);
        productRepository.save(product);
        return OrderDTO.fromEntity(order);
    }

    /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param request the order request containing updated order details
     * @param currentUser the current user making the request
     * @return the updated order
     * @throws PermissionDeniedDataAccessException if the user does not have permission to update the order
     * @throws EntityNotFoundException if the order is not found
     */
    @CacheEvict(value = {"userOrders"}, key = "#orderId")
    public OrderDTO updateOrder(Long orderId, OrderRequest request, User currentUser) {
        if (isUserOrder(orderId, currentUser)) {
            throw new PermissionDeniedDataAccessException("You don't have permission to update this order", null);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setQuantity(request.getQuantity());
        order.setDescription(request.getDescription());
        order.setStatus(request.getStatus());
        order.setPrice(order.getProduct().getPrice() * request.getQuantity());
        return OrderDTO.fromEntity(orderRepository.save(order));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @param currentUser the current user making the request
     * @return the retrieved order
     * @throws PermissionDeniedDataAccessException if the user does not have permission to get the order
     * @throws EntityNotFoundException if the order is not found
     */
    @Override
    @Cacheable(key = "#orderId")
    public OrderDTO getOrderById(Long orderId, User currentUser) {
        if (isUserOrder(orderId, currentUser)) {
            throw new PermissionDeniedDataAccessException("You don't have permission to get this order", null);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return OrderDTO.fromEntity(order);
    }

    /**
     * Retrieves a list of orders based on filters.
     *
     * @param status the status of the orders to filter by
     * @param minPrice the minimum price of the orders to filter by
     * @param maxPrice the maximum price of the orders to filter by
     * @return the list of filtered orders
     */
    @Cacheable(key = "'filtered' + T(java.util.Objects).hash(#status, #minPrice, #maxPrice)")
    public List<OrderDTO> getFilteredOrders(
            @Nullable OrderStatus status,
            @Nullable Double minPrice,
            @Nullable Double maxPrice
    ) {
        validatePriceRange(minPrice, maxPrice);
        return orderRepository.findFilteredOrders(status, minPrice, maxPrice)
                .stream()
                .map(OrderDTO::fromEntity)
                .toList();
    }

    /**
     * Validates the price range.
     *
     * @param min the minimum price
     * @param max the maximum price
     * @throws IllegalArgumentException if the minimum price is greater than the maximum price
     */
    public void validatePriceRange(Double min, Double max) {
        if (min != null && max != null && min > max) {
            throw new IllegalArgumentException("Invalid price range");
        }
    }

    /**
     * Soft deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     * @throws EntityNotFoundException if the order is not found
     */
    @CacheEvict(value = {"orders", "userOrders"}, key = "#orderId")
    public void softDelete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setDeleted(true);
        orderRepository.save(order);
    }
}