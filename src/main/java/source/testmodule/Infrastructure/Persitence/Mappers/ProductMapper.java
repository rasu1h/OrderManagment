package source.testmodule.Infrastructure.Persitence.Mappers;

import org.mapstruct.Mapper;
import source.testmodule.Domain.Model.Order;
import source.testmodule.Domain.Model.Product;
import source.testmodule.Infrastructure.Persitence.Entity.OrderJpaEntity;
import source.testmodule.Infrastructure.Persitence.Entity.ProductJpaEntity;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.ProductDTO;

public interface ProductMapper {
    Product toDomain(ProductJpaEntity entity);
    ProductDTO toDTO(Product domain);
}
