package source.testmodule.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import source.testmodule.Presentation.DTO.ProductDTO;
import source.testmodule.Presentation.DTO.Requests.ProductRequest;
import source.testmodule.Application.Services.ProductService;

@RestController
@RequiredArgsConstructor
@Schema(description = "Product Controller")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Adding a new product", description = "Adding a new Product for a Production")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created product", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/products/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest productRequest) {
        ProductDTO productDTO = productService.createProduct(productRequest);
        return ResponseEntity.ok(productDTO);
    }
}
