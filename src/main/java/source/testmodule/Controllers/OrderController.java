package source.testmodule.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import source.testmodule.DTO.OrderDTO;
import source.testmodule.DTO.Requests.OrderRequest;
import source.testmodule.Services.OrderService;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Schema(description = "Order Controller")
public class OrderController extends BaseController {
    private final OrderService orderService;

    @PostMapping("/users/create")
    @Operation(summary = "Create a new order", description = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Creating a new order");
        OrderDTO orderDTO = orderService.createOrder(orderRequest, userId);
        return ResponseEntity.ok(orderDTO);
    }
}