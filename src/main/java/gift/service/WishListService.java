package gift.service;

import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListService {

  WishListResponseDto addToWishList(Long memberId, WishListCreateRequestDto wishListCreateRequestDto);

  Page<WishListResponseDto> getWishList(Long memberId, Pageable pageable);

  WishListResponseDto updateQuantity(Long memberId, WishListUpdateRequestDto wishListUpdateRequestDto);

  void removeFromWishList(Long memberId, Long productId);

  void clearWishList(Long memberId);
}
