package gift.controller;

import gift.dto.ProductOptionResponseDTO;
import gift.dto.ProductRequestDTO;
import gift.model.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public void addProduct(@Valid @RequestBody ProductRequestDTO dto) {
        productService.addProduct(dto);
    }

    @GetMapping("/products/{productId}/options")
    public List<ProductOptionResponseDTO> getOptions(@PathVariable Long productId) {
        return productService.getOptions(productId);
    }

    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PatchMapping("/products/{id}")
    public void updateProduct(@Valid @PathVariable Long id, @RequestBody Product product) {
        productService.updateProduct(product);
    }

}