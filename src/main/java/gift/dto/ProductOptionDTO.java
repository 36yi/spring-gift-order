package gift.dto;

import jakarta.validation.constraints.*;

public record ProductOptionDTO(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]\\+\\-\\&/_]+$", message = "허용되지 않은 특수문자가 포함되어 있습니다.")
        String name,

        @NotNull
        @Min(1)
        @Max(99999999)
        Integer quantity
) {}