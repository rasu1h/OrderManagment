package source.testmodule.Presentation.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;

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
     * @param productJpaEntity
     * @return
     */
    public static ProductDTO fromEntity(ProductJpaEntity productJpaEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productJpaEntity.getId());
        productDTO.setName(productJpaEntity.getName());
        productDTO.setPrice(productJpaEntity.getPrice());
        productDTO.setQuantity(productJpaEntity.getQuantity());
        return productDTO;
    }
}
