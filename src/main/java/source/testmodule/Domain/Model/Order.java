package source.testmodule.Domain.Model;



import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long id;
    private String description;
    private Double price;
    private Integer quantity;
    private OrderStatus status;
    private User user;
    private Product product;



    public boolean isValid() {
        return price != null && price > 0;
    }


}