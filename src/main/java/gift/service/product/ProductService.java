package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  List<ProductResponseDto> findAllProduct();

  Page<ProductResponseDto> findAllProductAsPage(Pageable pageable);

  ProductResponseDto findProductById(Long id);

  ProductResponseDto createProduct(ProductRequestDto requestDto);

  ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

  void deleteProduct(Long id);

}
