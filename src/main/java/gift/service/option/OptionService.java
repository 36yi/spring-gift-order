package gift.service.option;

import gift.dto.option.OptionRequestDto;
import gift.dto.option.OptionResponseDto;
import java.util.List;

public interface OptionService {

  List<OptionResponseDto> findByProductId(Long productId);

  OptionResponseDto createOption(Long productId, OptionRequestDto requestDto);

  void deleteAllOption(Long productId);

  void deleteByOptionId(Long productId, Long optionId);
}
