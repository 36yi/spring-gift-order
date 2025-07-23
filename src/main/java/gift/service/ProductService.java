package gift.service;

import gift.dto.ProductAdminRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  ProductResponseDto createProduct(ProductRequestDto productRequestDto);

  ProductResponseDto createAdminProduct(ProductAdminRequestDto productAdminRequestDto);

  Page<ProductResponseDto> searchAllProducts(Pageable pageable);

  ProductResponseDto searchProductById(Long id);

  ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

  ProductResponseDto updateAdminProduct(Long id, ProductAdminRequestDto productAdminRequestDto);

  void deleteProduct(Long id);
}
