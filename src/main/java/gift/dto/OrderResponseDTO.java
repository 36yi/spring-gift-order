package gift.dto;

import gift.model.Order;

public record OrderResponseDTO(
        Long id,
        Long optionId,
        int quantity,
        String orderDateTime,
        String message
) {
    public OrderResponseDTO(Order order) {
        this(
                order.getId(),
                order.getProductOption().getId(),
                order.getQuantity(),
                order.getOrderDateTime().toString(),
                order.getMessage()
        );
    }
}