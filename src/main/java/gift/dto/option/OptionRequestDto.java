package gift.dto.option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequestDto {

  @NotBlank(message = "이름은 필수입니다.")
  @Size(max = 50, message = "이름은 최대 50글자입니다")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]+$", message = "한글, 영문자, 숫자, ( ), [ ], +, -, &, /, _ 만 입력 가능(공백포함)")
  private String name;

  @Min(value = 0, message = "옵션 수량은 최소 1개 이상입니다.")
  @Max(value = 100000000, message = "옵션 수량은 1억 미만입니다.")
  private int quantity;

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }
}
