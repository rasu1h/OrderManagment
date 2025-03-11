package source.testmodule.Infrastructure.Persitence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "products")
public class ProductJpaEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public ProductJpaEntity(String name, BigDecimal price, int i) {
        this.name = name;
        this.price = price;
        this.quantity = i;
    }
}
