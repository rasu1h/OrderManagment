package source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;

import java.util.List;

public  interface JpaOrderRepository extends JpaRepository<OrderJpaEntity, Long> {
    /**
     * Find all orders by product and status.
     * Filter by Price.
     * @param status
     * @param minPrice
     * @param maxPrice
     * @return
     */
    @Query("""
                SELECT o FROM OrderJpaEntity o\s
                WHERE (:status IS NULL OR o.status = :status)
                AND (:minPrice IS NULL OR o.price >= :minPrice)
                AND (:maxPrice IS NULL OR o.price <= :maxPrice)
                AND o.deleted = false
                                     \s""")
    List<OrderJpaEntity> findFilteredOrders(
            @Param("status") OrderStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}