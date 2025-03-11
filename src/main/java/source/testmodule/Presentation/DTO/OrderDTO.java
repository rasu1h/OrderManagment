package source.testmodule.Presentation.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Domain.Enums.OrderStatus;

/**
 * DTO class for the order response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO response for order")
public class OrderDTO {
    private Long id;
    private String description;
    private Double price;
    private Integer quantity;
    private OrderStatus status;
    private Long users;
    private Long products;

    public OrderDTO(String invalidPriceRange) {
        description = invalidPriceRange;
    }

    /**
     * Converts the entity to the DTO.
     * @param orderJpaEntity
     * @return
     */
    public static OrderDTO fromEntity(OrderJpaEntity orderJpaEntity) {  // converting from entity to DTO response
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderJpaEntity.getId());
        orderDTO.setDescription(orderJpaEntity.getDescription());
        orderDTO.setPrice(orderJpaEntity.getPrice());
        orderDTO.setStatus(orderJpaEntity.getStatus());
        orderDTO.setQuantity(orderJpaEntity.getQuantity());
        orderDTO.setUsers(orderJpaEntity.getUserJpaEntity().getId());
        orderDTO.setProducts(orderJpaEntity.getProductJpaEntity().getId());
        return orderDTO;
    }
}
