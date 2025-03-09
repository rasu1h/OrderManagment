package source.testmodule.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.testmodule.Infrastructure.Configurations.Security.CurrentUser;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Application.Services.OrderService;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Schema(description = "Order Controller")
public class OrderController{
    private final OrderService orderService;

    @PostMapping("/users/create")
    @Operation(summary = "Create a new order", description = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest
                                              , @CurrentUser User currentUser
    ) {
        log.info("Creating a new order");
        OrderDTO orderDTO = orderService.createOrder(orderRequest, currentUser);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order got successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }



}