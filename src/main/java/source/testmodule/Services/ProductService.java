package source.testmodule.Services;

import source.testmodule.DTO.ProductDTO;
import source.testmodule.DTO.Requests.ProductRequest;

public interface ProductService {
    ProductDTO createProduct(ProductRequest productRequest);
}
