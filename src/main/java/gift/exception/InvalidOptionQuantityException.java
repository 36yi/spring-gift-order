package gift.exception;

public class InvalidOptionQuantityException extends RuntimeException {
  public InvalidOptionQuantityException(int currentQuantity, int subtractAmount) {
    super("차감할 수 없는 수량입니다. 현재 수량: " + currentQuantity + ", 차감 시도 수량: " + subtractAmount +
        ". 차감할 경우 옵션의 남은 개수가 음수일 수 없습니다");
  }
}
