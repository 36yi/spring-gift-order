package gift.wish.dto;

import gift.product.entity.Product;
import gift.wish.entity.Wish;

import java.math.BigDecimal;

public record WishResponse(
        Long productId,
        String name,
        BigDecimal price,
        String imgUrl,
        int quantity
) {
    public static WishResponse from(Wish wish) {
        Product product = wish.getProduct();
        return new WishResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                wish.getQuantity()
        );
    }
}
