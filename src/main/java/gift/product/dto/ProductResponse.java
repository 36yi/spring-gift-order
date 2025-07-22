package gift.product.dto;

import gift.product.entity.Product;
import gift.wish.entity.Wish;

import java.math.BigDecimal;

public record ProductResponse (
        Long id,
        String name,
        BigDecimal price,
        String imgUrl,
        int quantity
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                1
        );
    }
    public static ProductResponse from(Wish wish) {
        Product product = wish.getProduct();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                wish.getQuantity()
        );
    }
}