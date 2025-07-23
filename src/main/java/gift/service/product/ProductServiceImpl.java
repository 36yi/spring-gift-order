package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.exception.NameHasKakaoException;
import gift.exception.notfound.ProductNotFoundException;
import gift.repository.option.OptionJpaRepository;
import gift.repository.product.ProductJpaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductJpaRepository productRepository;
  private final OptionJpaRepository optionRepository;

  public ProductServiceImpl(ProductJpaRepository productRepository,
      OptionJpaRepository optionRepository) {
    this.productRepository = productRepository;
    this.optionRepository = optionRepository;
  }

  public List<ProductResponseDto> findAllProduct() {
    List<Product> allProduct = productRepository.findAll();
    List<ProductResponseDto> responseDtoList = new ArrayList<>();
    for (Product product : allProduct) {
      ProductResponseDto responseDto = new ProductResponseDto(product);
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  @Override
  public Page<ProductResponseDto> findAllProductAsPage(Pageable pageable) {
    return productRepository.findAll(pageable).map(ProductResponseDto::new);
  }

  public ProductResponseDto findProductById(Long id) {
    return productRepository.findById(id)
        .map(ProductResponseDto::new)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
  }

  @Transactional
  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    Product checkProduct = new Product(requestDto.getName(), requestDto.getPrice(),
        requestDto.getImageUrl());
    if (checkProduct.isNameHasWord("카카오") && !requestDto.getMdOk()) {
      throw new NameHasKakaoException("상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의가 필요합니다.");
    }
    Product product = productRepository.save(
        new Product(requestDto.getName(), requestDto.getPrice(),
            requestDto.getImageUrl()));
    return new ProductResponseDto(product);
  }

  @Transactional
  public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("product가 없습니다."));
    if (product.isNameHasWord("카카오") && !requestDto.getMdOk()) {
      throw new NameHasKakaoException("상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의가 필요합니다.");
    }
    product.update(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
    return new ProductResponseDto(id, product.getName(), product.getPrice(),
        product.getImageUrl());
  }

  @Transactional
  public void deleteProduct(Long id) {
    optionRepository.deleteByProductId(id);
    productRepository.deleteById(id);
  }
}
