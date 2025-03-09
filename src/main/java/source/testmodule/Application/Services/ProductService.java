package source.testmodule.Application.Services;

import source.testmodule.Presentation.DTO.ProductDTO;
import source.testmodule.Presentation.DTO.Requests.ProductRequest;

public interface ProductService {
    ProductDTO createProduct(ProductRequest productRequest);
}
