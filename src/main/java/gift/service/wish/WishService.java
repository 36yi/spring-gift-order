package gift.service.wish;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {

  List<WishResponseDto> findByMemberId(Long memberId);

  Page<WishResponseDto> findByMemberIdAsPage(Long memberId, Pageable pageable);

  WishResponseDto createWish(Long memberId, WishRequestDto requestDto);

  WishResponseDto updateQuantity(Long memberId, WishRequestDto requestDto);

  void deleteAllWish(Long memberId);

  void deleteByProductId(Long memberId, Long productId);
}
