package source.testmodule.Application.Services.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import source.testmodule.Presentation.DTO.ProductDTO;
import source.testmodule.Presentation.DTO.Requests.ProductRequest;
import source.testmodule.Domain.Entity.Product;
import source.testmodule.Infrastructure.Repository.ProductRepository;
import source.testmodule.Application.Services.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDTO createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        productRepository.save(product);
        return ProductDTO.fromEntity(product);
    }
}
