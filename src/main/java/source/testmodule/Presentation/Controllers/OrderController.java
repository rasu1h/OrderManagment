package source.testmodule.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Configurations.Security.CurrentUser;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Application.Services.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Schema(description = "Order Controller")
public class OrderController{
    private final OrderService orderService;

    @PostMapping("/my/create")
    @Operation(summary = "Create a new order", description = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POST/ Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest
                                              , @CurrentUser User currentUser
    ) {
        log.info("Creating a new order");
        OrderDTO orderDTO = orderService.createOrder(orderRequest, currentUser);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/my/{orderId}")
    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PUT/ Order updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @GetMapping("my/{orderId}")
    @Operation(summary = "Get order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET/ Order got successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get filtered orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public ResponseEntity<List<OrderDTO>> getOrders(
            @RequestParam(required = false, name = "status")
            @Parameter(description = "Order status (PENDING, COMPLETED, CANCELLED)")
            OrderStatus status,

            @RequestParam(required = false, name = "min_price")
            @Min(0)
            @Parameter(description = "Minimum price")
            Double minPrice,

            @RequestParam(required = false, name = "max_price")
            @Min(0)
            @Parameter(description = "Maximum price")
            Double maxPrice
    ) {
        log.info("Filtering orders: status={}, min={}, max={}", status, minPrice, maxPrice);
        return ResponseEntity.ok(orderService.getFilteredOrders(status, minPrice, maxPrice));
      }




}