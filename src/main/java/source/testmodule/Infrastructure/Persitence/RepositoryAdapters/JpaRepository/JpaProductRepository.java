package source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductJpaEntity, Long> {
    ProductJpaEntity findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByPrice(Double price);

}
