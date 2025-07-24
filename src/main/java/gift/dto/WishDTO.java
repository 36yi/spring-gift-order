package gift.dto;

import gift.model.Wish;

public class WishDTO {
    private Long wishId;
    private Long productId;
    private String productName;

    public WishDTO(Wish wish) {
        this.wishId = wish.getId();
        this.productId = wish.getProduct().getId();
        this.productName = wish.getProduct().getName();
    }
    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}