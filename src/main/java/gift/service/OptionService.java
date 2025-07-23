package gift.service;

import gift.dto.OptionInfoResponseDto;
import gift.dto.OptionRequestDto;

import gift.dto.OptionSubtractRequestDto;
import java.util.List;

public interface OptionService {

  List<OptionInfoResponseDto> getOptionsByProductId(Long productId);

  OptionInfoResponseDto addOptionToProduct(Long productId, OptionRequestDto dto);

  OptionInfoResponseDto subtractQuantity(Long productId, OptionSubtractRequestDto dto);

  OptionInfoResponseDto addDefaultOption(Long productId, String baseName);
}
