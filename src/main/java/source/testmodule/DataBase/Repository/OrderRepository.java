package source.testmodule.DataBase.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import source.testmodule.DataBase.Entity.Order;
import source.testmodule.DataBase.Entity.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByProduct(Product productId);
    List<Order> findByUserId(Long userId);

}
