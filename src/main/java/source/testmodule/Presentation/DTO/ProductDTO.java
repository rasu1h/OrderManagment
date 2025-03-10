package source.testmodule.Presentation.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import source.testmodule.Domain.Entity.Product;

/**
 * DTO class for the product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO response products")
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;

    /**
     * Converts the entity to the DTO.
     * @param product
     * @return
     */
    public static ProductDTO fromEntity(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        return productDTO;
    }
}
