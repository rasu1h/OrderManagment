package source.testmodule.Services.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import source.testmodule.DTO.ProductDTO;
import source.testmodule.DTO.Requests.ProductRequest;
import source.testmodule.DataBase.Entity.Product;
import source.testmodule.DataBase.Repository.ProductRepository;
import source.testmodule.Services.ProductService;

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
