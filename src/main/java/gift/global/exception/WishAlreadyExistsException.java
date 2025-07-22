package gift.global.exception;

import gift.product.entity.Product;

public class WishAlreadyExistsException extends RuntimeException {
  public WishAlreadyExistsException(Product product) {
    super("이미 위시리스트에 있는 상품입니다: " + product.getName());
  }
  public WishAlreadyExistsException(String message) {
    super(message);
  }
}
