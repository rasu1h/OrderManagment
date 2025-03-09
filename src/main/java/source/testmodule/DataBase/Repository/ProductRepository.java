package source.testmodule.DataBase.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.DataBase.Entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByPrice(Double price);
    List<Product> findByPriceLessThanEqualAndPriceGreaterThanEqual(Double min, Double max); // min <= price <= max
    List<Product> findByPriceLessThanEqual(Double max);                                     // price <= max
    List<Product> findByPriceGreaterThanEqual(Double min);                                  // price >= min
}
