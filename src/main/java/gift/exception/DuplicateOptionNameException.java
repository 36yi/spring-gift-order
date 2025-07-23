package gift.exception;

public class DuplicateOptionNameException extends RuntimeException {
  public DuplicateOptionNameException(Long productId, String optionName) {
    super("상품 ID = " + productId + " 에 이미 '" + optionName + "' 이름의 옵션이 존재합니다.");
  }
}
