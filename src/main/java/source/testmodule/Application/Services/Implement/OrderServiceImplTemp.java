//package source.testmodule.Application.Services.Implement;
//
//import jakarta.annotation.Nullable;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.Caching;
//import org.springframework.dao.PermissionDeniedDataAccessException;
//import org.springframework.stereotype.Service;
//import source.testmodule.Domain.Model.Order;
//import source.testmodule.Domain.Repository.OrderRepositoryPort;
//import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;
//import source.testmodule.Domain.Enums.OrderStatus;
//import source.testmodule.Presentation.DTO.OrderDTO;
//import source.testmodule.Presentation.DTO.Requests.OrderRequest;
//import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
//import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
//import source.testmodule.Infrastructure.Persitence.JpaRepository.JpaOrderRepository;
//import source.testmodule.Infrastructure.Persitence.JpaRepository.JpaProductRepository;
//import source.testmodule.Application.Services.OrderService;
//
//import java.util.List;
//import java.util.Objects;
//
///**
// * Implementation of the OrderService interface.
// * Provides methods for managing orders.
// */
//@Service
//@RequiredArgsConstructor
//@CacheConfig(cacheNames = "ordersCache")
//public class OrderServiceImpl implements OrderService {
//    private final OrderRepositoryPort jpaOrderRepository;
//    private final JpaProductRepository jpaProductRepository;
//    private final CacheManager cacheManager;
//
//    /**
//     * Checks if the order belongs to the current user.
//     *
//     * @param orderId the ID of the order
//     * @param currentUserJpaEntity the current user
//     * @return true if the order does not belong to the user, false otherwise
//     * @throws EntityNotFoundException if the order is not found
//     */
//    boolean isUserOrder(Long orderId, UserJpaEntity currentUserJpaEntity) {
//        return jpaOrderRepository.findById(orderId)
//                .map(orderJpaEntity -> !Objects.equals(orderJpaEntity.getUserJpaEntity().getId(), currentUserJpaEntity.getId()))
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//    }
//
//    /**
//     * Creates a new order.
//     *
//     * @param orderRequest the order request containing order details
//     * @param currentUserJpaEntity the current user making the request
//     * @return the created order
//     * @throws RuntimeException if the product is not found or there is not enough quantity
//     * @throws IllegalArgumentException if the quantity is less than or equal to 0 or the description is too long
//     */
//    @Override
//    @Transactional
//    @Caching(evict = {
//            @CacheEvict(key = "'user_' + #currentUserJpaEntity.id"),
//            @CacheEvict(key = "'filtered'"),
//            @CacheEvict(key = "#result.id", condition = "#result != null")
//    })
//    public OrderDTO createOrder(OrderRequest orderRequest, UserJpaEntity currentUserJpaEntity) {
//        OrderJpaEntity orderJpaEntity = new OrderJpaEntity();
//        ProductJpaEntity productJpaEntity = jpaProductRepository.findById(orderRequest.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        if (productJpaEntity.getQuantity() < orderRequest.getQuantity()) {
//            throw new RuntimeException("Not enough quantity");
//        }
//        if (orderRequest.getQuantity() <= 0) {
//            throw new IllegalArgumentException("Quantity must be greater than 0");
//        }
//        if (orderRequest.getDescription().length() > 255) {
//            throw new IllegalArgumentException("Description is too long");
//        }
//        orderJpaEntity.setDescription(orderRequest.getDescription());
//        orderJpaEntity.setQuantity(orderRequest.getQuantity());
//        orderJpaEntity.setPrice(productJpaEntity.getPrice() * orderRequest.getQuantity());
//        orderJpaEntity.setProductJpaEntity(productJpaEntity);
//        orderJpaEntity.setUserJpaEntity(currentUserJpaEntity);
//        productJpaEntity.setQuantity(productJpaEntity.getQuantity() - orderRequest.getQuantity());
//
//        jpaOrderRepository.save(orderJpaEntity);
//        jpaProductRepository.save(productJpaEntity);
//        return OrderDTO.fromEntity(orderJpaEntity);
//    }
//
//    /**
//     * Updates an existing order.
//     *
//     * @param orderId the ID of the order to update
//     * @param request the order request containing updated order details
//     * @param currentUserJpaEntity the current user making the request
//     * @return the updated order
//     * @throws PermissionDeniedDataAccessException if the user does not have permission to update the order
//     * @throws EntityNotFoundException if the order is not found
//     */
//    @CacheEvict(value = {"userOrders"}, key = "#orderId")
//    public OrderDTO updateOrder(Long orderId, OrderRequest request, UserJpaEntity currentUserJpaEntity) {
//        if (isUserOrder(orderId, currentUserJpaEntity)) {
//            throw new PermissionDeniedDataAccessException("You don't have permission to update this order", null);
//        }
//        OrderJpaEntity orderJpaEntity = jpaOrderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//        orderJpaEntity.setQuantity(request.getQuantity());
//        orderJpaEntity.setDescription(request.getDescription());
//        orderJpaEntity.setStatus(request.getStatus());
//        orderJpaEntity.setPrice(orderJpaEntity.getProductJpaEntity().getPrice() * request.getQuantity());
//        return OrderDTO.fromEntity(jpaOrderRepository.save(orderJpaEntity));
//    }
//
//    /**
//     * Retrieves an order by its ID.
//     *
//     * @param orderId the ID of the order to retrieve
//     * @param currentUserJpaEntity the current user making the request
//     * @return the retrieved order
//     * @throws PermissionDeniedDataAccessException if the user does not have permission to get the order
//     * @throws EntityNotFoundException if the order is not found
//     */
//    @Override
//    @Cacheable(key = "#orderId")
//    public OrderDTO getOrderById(Long orderId, UserJpaEntity currentUserJpaEntity) {
//        if (isUserOrder(orderId, currentUserJpaEntity)) {
//            throw new PermissionDeniedDataAccessException("You don't have permission to get this order", null);
//        }
//        OrderJpaEntity orderJpaEntity = jpaOrderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//        return OrderDTO.fromEntity(orderJpaEntity);
//    }
//
//    /**
//     * Retrieves a list of orders based on filters.
//     *
//     * @param status the status of the orders to filter by
//     * @param minPrice the minimum price of the orders to filter by
//     * @param maxPrice the maximum price of the orders to filter by
//     * @return the list of filtered orders
//     */
//    @Cacheable(key = "'filtered' + T(java.util.Objects).hash(#status, #minPrice, #maxPrice)")
//    public List<OrderDTO> getFilteredOrders(
//            @Nullable OrderStatus status,
//            @Nullable Double minPrice,
//            @Nullable Double maxPrice
//    ) {
//        validatePriceRange(minPrice, maxPrice);
//        return jpaOrderRepository.findFilteredOrders(status, minPrice, maxPrice)
//                .stream()
//                .map(OrderDTO::fromEntity)
//                .toList();
//    }
//
//    /**
//     * Validates the price range.
//     *
//     * @param min the minimum price
//     * @param max the maximum price
//     * @throws IllegalArgumentException if the minimum price is greater than the maximum price
//     */
//    public void validatePriceRange(Double min, Double max) {
//        if (min != null && max != null && min > max) {
//            throw new IllegalArgumentException("Invalid price range");
//        }
//    }
//
//    /**
//     * Soft deletes an order by its ID.
//     *
//     * @param orderId the ID of the order to delete
//     * @throws EntityNotFoundException if the order is not found
//     */
//    @CacheEvict(value = {"orders", "userOrders"}, key = "#orderId")
//    public void softDelete(Long orderId) {
//        Order order = jpaOrderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//        jpaOrderRepository.delete(order);
//    }
//}
