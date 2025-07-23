package gift.dto;

import gift.domain.WishList;

public record WishListResponseDto(
    Long memberId,
    Long productId,
    Integer quantity
) {
  public WishListResponseDto(WishList wishList) {
    this(
        wishList.getMember().getId(),
        wishList.getProduct().getId(),
        wishList.getQuantity()
    );
  }
}
