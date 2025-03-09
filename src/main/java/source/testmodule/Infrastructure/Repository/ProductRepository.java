package source.testmodule.Infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByPrice(Double price);

}
