package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for the product request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание продукта")
public class ProductRequest {
    private String name;
    private Double price;
    private Integer quantity;
}
