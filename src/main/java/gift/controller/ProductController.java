package gift.controller;

import gift.dto.option.OptionRequestDto;
import gift.dto.option.OptionResponseDto;
import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.service.option.OptionService;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;
  private final OptionService optionService;

  public ProductController(ProductService productService, OptionService optionService) {
    this.productService = productService;
    this.optionService = optionService;
  }

  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> findAllProduct(
      @PageableDefault(page = 0, size = 5) Pageable pageable) {
    return new ResponseEntity<>(productService.findAllProductAsPage(pageable), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
    return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
  }

  @GetMapping("/{id}/options")
  public ResponseEntity<List<OptionResponseDto>> findOptionById(@PathVariable Long id) {
    return new ResponseEntity<>(optionService.findByProductId(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> createProduct(
      @Valid @RequestBody ProductRequestDto requestDto) {
    return new ResponseEntity<>(productService.createProduct(requestDto), HttpStatus.CREATED);
  }

  @PostMapping("/{id}/options")
  public ResponseEntity<OptionResponseDto> createOption(@PathVariable Long id,
      @Valid @RequestBody OptionRequestDto requestDto) {
    return new ResponseEntity<>(optionService.createOption(id, requestDto), HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
      @Valid @RequestBody ProductRequestDto requestDto) {
    return new ResponseEntity<>(productService.updateProduct(id, requestDto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{id}/options")
  public ResponseEntity<OptionResponseDto> deleteAllOption(@PathVariable Long id) {
    optionService.deleteAllOption(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{productId}/options/{optionId}")
  public ResponseEntity<OptionResponseDto> deleteByOptionId(
      @PathVariable(name = "productId") Long productId,
      @PathVariable(name = "optionId") Long optionId) {
    optionService.deleteByOptionId(productId, optionId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
