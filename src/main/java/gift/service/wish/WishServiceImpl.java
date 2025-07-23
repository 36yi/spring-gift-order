package gift.service.wish;

import gift.dto.product.ProductResponseDto;
import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.notfound.MemberNotFoundException;
import gift.exception.notfound.ProductNotFoundException;
import gift.exception.notfound.WishListNotFoundException;
import gift.repository.member.MemberJpaRepository;
import gift.repository.product.ProductJpaRepository;
import gift.repository.wish.WishJpaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

  private WishJpaRepository wishRepository;

  private MemberJpaRepository memberJpaRepository;

  private ProductJpaRepository productRepository;

  public WishServiceImpl(WishJpaRepository wishRepository, MemberJpaRepository memberJpaRepository,
      ProductJpaRepository productRepository) {
    this.wishRepository = wishRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.productRepository = productRepository;
  }

  public List<WishResponseDto> findByMemberId(Long memberId) {
    List<Wish> allWish = wishRepository.findByMemberId(memberId);
    List<WishResponseDto> responseDtoList = new ArrayList<>();
    for (Wish wish : allWish) {
      Optional<Product> productById = productRepository.findById(wish.getProduct().getId());
      Product product = productById.get();
      WishResponseDto responseDto = new WishResponseDto(wish.getId(),
          new ProductResponseDto(wish.getProduct()),
          wish.getQuantity());
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  @Override
  public Page<WishResponseDto> findByMemberIdAsPage(Long memberId, Pageable pageable) {
    return wishRepository.findByMemberId(memberId, pageable).map(WishResponseDto::new);
  }

  @Transactional
  public WishResponseDto createWish(Long memberId, WishRequestDto requestDto) {
    Member member = memberJpaRepository.findById(memberId)
        .orElseThrow(() -> new MemberNotFoundException("멤버가 없습니다"));
    Product product = productRepository.findById(requestDto.getProductId())
        .orElseThrow(() -> new ProductNotFoundException("위시 리스트에 넣으려는 상품이 없습니다."));

    Wish wish = wishRepository.save(new Wish(member, product, requestDto.getQuantity()));
    return new WishResponseDto(wish.getId(), new ProductResponseDto(wish.getProduct()),
        wish.getQuantity());
  }

  @Transactional
  public WishResponseDto updateQuantity(Long memberId, WishRequestDto requestDto) {
    Optional<Product> productById = productRepository.findById(requestDto.getProductId());
    Product product = productById.orElseThrow(
        () -> new ProductNotFoundException("위시 리스트에 넣으려는 상품이 없습니다."));

    Wish wish = wishRepository.findByMemberIdAndProductId(memberId,
        requestDto.getProductId()).orElseThrow(() -> new WishListNotFoundException("위시 리스트가 없습니다"));
    wish.updateQuantity(requestDto.getQuantity());

    return new WishResponseDto(wish.getId(), new ProductResponseDto(wish.getProduct()),
        wish.getQuantity());
  }

  @Transactional
  public void deleteAllWish(Long memberId) {
    wishRepository.deleteByMemberId(memberId);
  }

  @Transactional
  public void deleteByProductId(Long memberId, Long productId) {
    wishRepository.deleteByMemberIdAndProductId(memberId, productId);
  }
}
