package source.testmodule.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание заказа")
public class OrderRequest {
    private String description;
    private Integer quantity;
    private Long productId;
}
