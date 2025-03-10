package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import source.testmodule.Domain.Enums.OrderStatus;

/**
 * DTO class for the order request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание заказа")
public class OrderRequest {
    private String description;
    private OrderStatus status;
    private Integer quantity;
    private Long productId;
}
