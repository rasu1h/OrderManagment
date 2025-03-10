package source.testmodule.Infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Entity.Order;
import source.testmodule.Domain.Entity.Product;
import source.testmodule.Domain.Enums.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Find all orders by product and status.
     * Filter by Price.
     * @param status
     * @param minPrice
     * @param maxPrice
     * @return
     */
    @Query("""
        SELECT o FROM Order o\s
        WHERE (:status IS NULL OR o.status = :status)
        AND (:minPrice IS NULL OR o.price >= :minPrice)
        AND (:maxPrice IS NULL OR o.price <= :maxPrice)
        AND o.deleted = false
   \s""")
    List<Order> findFilteredOrders(
            @Param("status") OrderStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
