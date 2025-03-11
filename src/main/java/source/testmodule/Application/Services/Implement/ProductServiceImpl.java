package source.testmodule.Application.Services.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import source.testmodule.Presentation.DTO.ProductDTO;
import source.testmodule.Presentation.DTO.Requests.ProductRequest;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.RepositoryAdapters.JpaRepository.JpaProductRepository;
import source.testmodule.Application.Services.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final JpaProductRepository jpaProductRepository;

    @Override
    public ProductDTO createProduct(ProductRequest productRequest) {
        ProductJpaEntity productJpaEntity = new ProductJpaEntity();
        productJpaEntity.setName(productRequest.getName());
        productJpaEntity.setPrice(productRequest.getPrice());
        productJpaEntity.setQuantity(productRequest.getQuantity());
        jpaProductRepository.save(productJpaEntity);
        return ProductDTO.fromEntity(productJpaEntity);
    }
}
