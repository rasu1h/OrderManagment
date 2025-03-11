package source.testmodule.Infrastructure.Persitence.RepositoryAdapters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import source.testmodule.Domain.Model.Product;
import source.testmodule.Domain.Repository.ProductRepositoryPort;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.ProductMapper;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaProductRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort{

    private final JpaProductRepository productRepository;
    private final ProductMapper productMapper;



    @Override
    public Product findByName(String name) {
        return productMapper.toDomain(productRepository.findByName(name)) ;
    }

    @Override
    public Boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Boolean existsByPrice(Double price) {
        return productRepository.existsByPrice(price);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public void save(Product product) {
        ProductJpaEntity productJpaEntity = productRepository.findById(product.getId()).get();
        productJpaEntity.setName(product.getName());
        productJpaEntity.setPrice(product.getPrice());
        productJpaEntity.setQuantity(product.getQuantity());

        productRepository.save(productJpaEntity);
    }

    @Override
    public void saveAll(List<ProductJpaEntity> products) {
        productRepository.saveAll(products);
    }


}
