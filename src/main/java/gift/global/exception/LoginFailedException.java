package gift.global.exception;

public class LoginFailedException extends RuntimeException {
  public LoginFailedException() {
    super(ErrorCode.LOGIN_FAILED.getMessage());
  }

  public LoginFailedException(String message) {
    super(message);
  }
}
