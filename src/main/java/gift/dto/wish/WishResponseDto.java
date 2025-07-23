package gift.dto.wish;

import gift.dto.product.ProductResponseDto;
import gift.entity.Wish;

public class WishResponseDto {

  private Long id;
  private ProductResponseDto productResponseDto;
  private Long quantity;

  public WishResponseDto(Long id, ProductResponseDto productResponseDto, Long quantity) {
    this.id = id;
    this.productResponseDto = productResponseDto;
    this.quantity = quantity;
  }

  public WishResponseDto(Wish wish) {
    this(wish.getId(), new ProductResponseDto(wish.getProduct()), wish.getQuantity());
  }

  public Long getId() {
    return id;
  }

  public ProductResponseDto getProductResponseDto() {
    return productResponseDto;
  }

  public Long getQuantity() {
    return quantity;
  }
}
