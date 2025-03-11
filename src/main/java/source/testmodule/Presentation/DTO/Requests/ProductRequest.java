package source.testmodule.Presentation.DTO.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO class for the product request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for product")
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
