package source.testmodule.Infrastructure.Persitence.Mappers.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import source.testmodule.Domain.Model.Product;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Infrastructure.Persitence.Mappers.ProductMapper;
import source.testmodule.Presentation.DTO.ProductDTO;

@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null or not found " + this);
        }
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setPrice(entity.getPrice());
        product.setQuantity(entity.getQuantity());
        return product;
    }



    @Override
    public ProductDTO toDTO(Product domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Domain cannot be null or not found " + this);
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(domain.getId());
        productDTO.setName(domain.getName());
        productDTO.setPrice(domain.getPrice());
        productDTO.setQuantity(domain.getQuantity());
        return productDTO;
    }
}
