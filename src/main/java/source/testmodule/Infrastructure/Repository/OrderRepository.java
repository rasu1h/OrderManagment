package source.testmodule.Infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Entity.Order;
import source.testmodule.Domain.Entity.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByProduct(Product productId);
    List<Order> findByUserId(Long userId);
    List<Order> findByPriceLessThanEqualAndPriceGreaterThanEqual(Double min, Double max); // min <= price <= max
    List<Order> findByPriceLessThanEqual(Double max);                                     // price <= max
    List<Order> findByPriceGreaterThanEqual(Double min);
}
