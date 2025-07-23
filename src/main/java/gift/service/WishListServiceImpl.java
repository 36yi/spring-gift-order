package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishList;
import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import gift.exception.ProductNotFoundException;
import gift.exception.WishListItemNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListServiceImpl implements WishListService {

  private final Integer ZERO_CNT = 0;

  private final WishListRepository wishListRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  private final ProductService productService;

  public WishListServiceImpl(WishListRepository wishListRepository, MemberRepository memberRepository, ProductRepository productRepository, ProductService productService) {
    this.wishListRepository = wishListRepository;
    this.memberRepository = memberRepository;
    this.productRepository = productRepository;
    this.productService = productService;
  }

  private void validateProductExists(Long productId) {
    productService.searchProductById(productId);
  }

  @Override
  @Transactional
  public WishListResponseDto addToWishList(Long memberId, WishListCreateRequestDto dto) {
    Long productId = dto.productId();
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    Optional<Member> opMember = memberRepository.findById(memberId);

    Member member = opMember.orElseThrow(() ->
        new NoSuchElementException("해당 ID = " + memberId + " 의 회원이 존재하지 않습니다.")
    );

    Optional<WishList> optional = wishListRepository.findByMemberAndProduct(member, product);

    if (optional.isPresent()) {
      WishList wishList = optional.get();
      Integer updatedQuantity = wishList.getQuantity() + dto.quantity();
      wishList.updateQuantity(updatedQuantity);
      return new WishListResponseDto(memberId, productId, updatedQuantity);
    }

    WishList newWishList = new WishList(member, product, dto.quantity());
    WishList saved = wishListRepository.save(newWishList);
    return new WishListResponseDto(memberId, productId, dto.quantity());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<WishListResponseDto> getWishList(Long memberId, Pageable pageable) {
    Optional<Member> opMember = memberRepository.findById(memberId);

    Member member = opMember.orElseThrow(() ->
        new NoSuchElementException("해당 ID = " + memberId + " 의 회원이 존재하지 않습니다.")
    );

    return wishListRepository.findAllByMember(member, pageable)
        .map(WishListResponseDto::new);
  }

  @Override
  @Transactional
  public WishListResponseDto updateQuantity(Long memberId, WishListUpdateRequestDto dto) {
    Long productId = dto.productId();

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    validateProductExists(productId);

    Optional<Member> opMember = memberRepository.findById(memberId);

    Member member = opMember.orElseThrow(() ->
        new NoSuchElementException("해당 ID = " + memberId + " 의 회원이 존재하지 않습니다.")
    );

    Integer quantity = dto.quantity();

    WishList wishList = wishListRepository.findByMemberAndProduct(member, product)
        .orElseThrow(() -> new WishListItemNotFoundException(memberId, productId));

    if (quantity == ZERO_CNT) {
      wishListRepository.deleteByMemberAndProduct(member, product);
      return new WishListResponseDto(memberId, productId, ZERO_CNT);
    }

    wishList.updateQuantity(dto.quantity());
    return new WishListResponseDto(wishList);
  }

  @Override
  @Transactional
  public void removeFromWishList(Long memberId, Long productId) {
    validateProductExists(productId);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    Optional<Member> opMember = memberRepository.findById(memberId);

    Member member = opMember.orElseThrow(() ->
        new NoSuchElementException("해당 ID = " + memberId + " 의 회원이 존재하지 않습니다.")
    );

    WishList wishList = wishListRepository.findByMemberAndProduct(member, product)
        .orElseThrow(() -> new WishListItemNotFoundException(memberId, productId));

    wishListRepository.delete(wishList);
  }

  @Override
  @Transactional
  public void clearWishList(Long memberId) {
    Optional<Member> opMember = memberRepository.findById(memberId);

    Member member = opMember.orElseThrow(() ->
        new NoSuchElementException("해당 ID = " + memberId + " 의 회원이 존재하지 않습니다.")
    );

    wishListRepository.deleteAllByMember(member);
  }
}
