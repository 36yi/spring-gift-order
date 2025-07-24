package gift.controller;

import gift.dto.ProductOptionDTO;
import gift.dto.ProductOptionResponseDTO;
import gift.dto.ProductRequestDTO;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    public ProductController(ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
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
        return productRepository.findAll(pageable);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + id));
    }

    @PostMapping("/products")
    public void addProduct(@Valid @RequestBody ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setImage(dto.image());

        if (!dto.name().contains("카카오")) {
            product.setMdApproved(true);
        }

        productRepository.save(product);

        for (ProductOptionDTO optDto : dto.options()) {
            ProductOption option = new ProductOption(product, optDto.name(), optDto.quantity());
            productOptionRepository.save(option);
        }
    }

    @GetMapping("/products/{productId}/options")
    public List<ProductOptionResponseDTO> getOptions(@PathVariable Long productId) {
        List<ProductOption> options = productOptionRepository.findByProductId(productId);
        return options.stream()
                .map(opt -> new ProductOptionResponseDTO(opt.getId(), opt.getName(), opt.getQuantity()))
                .toList();
    }

    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @PatchMapping("/products/{id}")
    public void updateProduct(@Valid @PathVariable Long id, @RequestBody Product product) {
        if(!product.getName().contains("카카오")){
            product.setMdApproved(true);
        }else{
            product.setMdApproved(false);
        }
        productRepository.save(product);
    }

}