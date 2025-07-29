package gift.dto;

public record OrderRequestDTO(
        Long optionId,
        int quantity,
        String message
) {}