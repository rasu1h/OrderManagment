package source.testmodule.Presentation.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import source.testmodule.Domain.Entity.Order;
import source.testmodule.Domain.Enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO ответ заказа")
public class OrderDTO {
    private Long id;
    private String description;
    private Double price;
    private Integer quantity;
    private OrderStatus status;
    private Long users;
    private Long products;

    public static OrderDTO fromEntity(Order order) {  // converting from entity to DTO response
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setDescription(order.getDescription());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setUsers(order.getUser().getId());
        orderDTO.setProducts(order.getProduct().getId());
        return orderDTO;
    }
}
