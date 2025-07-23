package gift.dto;

import jakarta.validation.constraints.*;

public record OptionRequestDto(

    @NotBlank(message = "옵션 이름은 필수입니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 가능합니다.")
    @Pattern(
        regexp = "^[\\w\\s()\\[\\]\\+\\-\\&/_가-힣]+$",
        message = "상품 이름에는 일부 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다."
    )
    String name,

    @NotNull(message = "옵션 수량은 필수입니다.")
    @Min(value = 1, message = "옵션 수량은 최소 1 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억 미만이어야 합니다.")
    Integer quantity

) {}
