package source.testmodule.Application.Services.Implement;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import source.testmodule.Application.Services.OrderService;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Model.Product;
import source.testmodule.Domain.Model.User;
import source.testmodule.Domain.Repository.OrderRepositoryPort;
import source.testmodule.Domain.Repository.ProductRepositoryPort;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.OrderMapper;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Enums.OrderStatus;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ordersCache")
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryPort orderRepository;
    private final ProductRepositoryPort productRepository;
    private final OrderMapper orderMapper;
    private final CacheManager cacheManager;


    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'user_' + #currentUser.id"),
            @CacheEvict(key = "'filtered'"),
            @CacheEvict(key = "#result.id", condition = "#result != null")
    })
    public OrderDTO createOrder(OrderRequest request, User currentUser) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(request.getProductId()) + " of Product not found"));

        validateOrderRequest(request, product);

        Order newOrder = Order.builder()
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .product(product)
                .user(currentUser)
                .status(OrderStatus.PENDING)
                .price(product.getPrice() * request.getQuantity())
                .build();

        Order savedOrder = orderRepository.save(newOrder);
        productRepository.save(product);

        return orderMapper.toDTO(savedOrder);
    }

        /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param request the order request containing updated order details
     * @param currentUserJpaEntity the current user making the request
     * @return the updated order
     * @throws PermissionDeniedDataAccessException if the user does not have permission to update the order
     * @throws EntityNotFoundException if the order is not found
     */
    @CacheEvict(value = {"userOrders"}, key = "#orderId")
    public OrderDTO updateOrder(Long orderId, OrderRequest request, User currentUserJpaEntity) {
        if (isUserOrder(orderId, currentUserJpaEntity)) {
            throw new PermissionDeniedDataAccessException("You don't have permission to update this order", null);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setQuantity(request.getQuantity());
        order.setDescription(request.getDescription());
        order.setStatus(request.getStatus());
        order.setPrice(order.getProduct().getPrice() * request.getQuantity());
        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    /**
     * Validates the order request.
     * @param request
     * @param product
     */
    private void validateOrderRequest(OrderRequest request, Product product) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(request.getQuantity() + " is less than or equal to 0");
        }

        if (request.getDescription().length() > 255) {
            throw new IllegalArgumentException(request.getDescription() + " is too long");
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(product.getId() + " Not enough quantity");
        }
    }

    /**
     * Retrieves an order by its ID.
     * @param orderId
     * @param currentUser
     * @return
     */
    @Override
    @Cacheable(key = "#orderId")
    public OrderDTO getOrderById(Long orderId, User currentUser) {
        if (!isUserOrder(orderId,currentUser)) {
            throw new PermissionDeniedDataAccessException("You don't have permission to update this order", null);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderMapper.toDTO(order);
    }


    /**
     * Retrieves a list of orders based on filters.
     *
     * @param status the status of the orders to filter by
     * @param minPrice the minimum price of the orders to filter by
     * @param maxPrice the maximum price of the orders to filter by
     * @return the list of filtered orders
     */
    @Override
    @Cacheable(key = "'filtered' + T(java.util.Objects).hash(#status, #minPrice, #maxPrice)")
    public List<OrderDTO> getFilteredOrders(
            @Nullable OrderStatus status,
            @Nullable Double minPrice,
            @Nullable Double maxPrice
    ) {
        validatePriceRange(minPrice, maxPrice);
        return orderRepository.findFilteredOrders(status, minPrice, maxPrice);
    }

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
        orderRepository.delete(order);
    }

        /**
     * Checks if the order belongs to the current user.
     *
     * @param orderId the ID of the order
     * @param currentUser the current user
     * @return true if the order does not belong to the user, false otherwise
     * @throws EntityNotFoundException if the order is not found
     */
    boolean isUserOrder(Long orderId, User currentUser) {
        Order jpaOrder = orderRepository.findById(orderId).orElseThrow();
        log.debug("UserJpaEntity in OrderJpaEntity: {}", jpaOrder.getUser()); // Should not be null
        return orderRepository.findById(orderId)
                .map(order -> !Objects.equals(order.getUser().getId(), currentUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
