package gift.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ProductRequestDTO(
        @NotBlank
        String name,

        @NotNull
        Integer price,

        String image,

        @NotNull
        @Size(min = 1, message = "옵션은 최소 1개 이상이어야 합니다.")
        List<@Valid ProductOptionDTO> options
) {}