package gift.wish.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @NotNull(message = "상품 ID는 비어있을 수 없습니다.")
        Long productId,

        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int quantity
) {
}