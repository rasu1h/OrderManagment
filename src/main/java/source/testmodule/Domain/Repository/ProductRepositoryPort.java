package source.testmodule.Domain.Repository;

import source.testmodule.Domain.Model.Product;

import java.util.Optional;

public interface ProductRepositoryPort {
    Product findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByPrice(Double price);
    Optional <Product> findById(Long id);

    void save(Product product);
}
