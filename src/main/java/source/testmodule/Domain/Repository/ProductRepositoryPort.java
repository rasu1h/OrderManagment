package source.testmodule.Domain.Repository;

import source.testmodule.Domain.Model.Product;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByPrice(Double price);
    Optional <Product> findById(Long id);

    void save(Product product);

    void saveAll(List<ProductJpaEntity> products);
}
