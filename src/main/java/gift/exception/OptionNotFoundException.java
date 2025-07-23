package gift.exception;

public class OptionNotFoundException extends RuntimeException {
  public OptionNotFoundException(Long productId, String optionName) {
    super("상품 ID = " + productId + " 에 해당하는 옵션 이름 '" + optionName + "' 이(가) 존재하지 않습니다.");
  }
}
