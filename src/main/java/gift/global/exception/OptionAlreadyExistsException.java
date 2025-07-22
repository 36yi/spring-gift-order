package gift.global.exception;

public class OptionAlreadyExistsException extends RuntimeException {
  private final Long productId;

  public OptionAlreadyExistsException(String option, Long productId) {
    super("이미 존재하는 옵션입니다: " + option);
    this.productId = productId;
  }

  public Long getProductId() {
    return productId;
  }
}

