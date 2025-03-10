package source.testmodule.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Configurations.Security.CurrentUser;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Application.Services.OrderService;

import java.util.List;

/**
 * Controller for handling operations related to orders.
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "orders", description = "Operations with orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Creates a new order for a user.
     *
     * @param orderRequest the order request containing order details
     * @param currentUser the Securty context giving the current user by authentication
     * @return the created order
     */
    @PostMapping("/my/create")
    @Operation(summary = "Create a new order", description = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POST/ Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest,
                                                @Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("Creating a new order");
        OrderDTO orderDTO = orderService.createOrder(orderRequest, currentUser);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param request the order request containing updated order details
     * @param currentUser the Securty context giving the current user by authentication
     * @return the updated order
     */
    @PutMapping("/my/{orderId}")
    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PUT/ Order updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId,
                                                @RequestBody @Valid OrderRequest request,
                                                @Parameter(hidden = true) @CurrentUser User currentUser) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request, currentUser));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @param currentUser the Securty context giving the current user by authentication
     * @return the retrieved order
     */
    @GetMapping("my/{orderId}")
    @Operation(summary = "Get order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET/ Order got successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId,
                                                 @Parameter(hidden = true) @CurrentUser User currentUser) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, currentUser));
    }

    /**
     * Retrieves a list of orders based on filters.
     *
     * @param status the status of the orders to filter by
     * @param minPrice the minimum price of the orders to filter by
     * @param maxPrice the maximum price of the orders to filter by
     * @return the list of filtered orders
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get filtered orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required", content = @Content(schema = @Schema(hidden = true))),
    })
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false, name = "status")
                                                    @Parameter(description = "Order status (PENDING, COMPLETED, CANCELLED)") OrderStatus status,
                                                    @RequestParam(required = false, name = "min_price")
                                                    @Min(0) @Parameter(description = "Minimum price") Double minPrice,
                                                    @RequestParam(required = false, name = "max_price")
                                                    @Min(0) @Parameter(description = "Maximum price") Double maxPrice) {
        log.info("Filtering orders: status={}, min={}, max={}", status, minPrice, maxPrice);
        return ResponseEntity.ok(orderService.getFilteredOrders(status, minPrice, maxPrice));
    }

    /**
     * Soft deletes an order by its ID (admin only).
     *
     * @param orderId the ID of the order to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order by ID", description = "Soft delete order (admin only)", security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - admin role required", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid/missing token", content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID of order to delete", required = true, example = "123") @PathVariable Long orderId) {
        orderService.softDelete(orderId);
        return ResponseEntity.noContent().build();
    }
}