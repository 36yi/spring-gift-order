package gift.controller;


import gift.config.LoginMember;
import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import gift.service.wish.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

  private WishService service;

  public WishController(WishService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<Page<WishResponseDto>> findWishByMemberId(@LoginMember Member member,
      @PageableDefault(page = 0, size = 5) Pageable pageable) {
    return new ResponseEntity<>(service.findByMemberIdAsPage(member.getId(), pageable),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<WishResponseDto> createWish(@LoginMember Member member,
      @Valid @RequestBody WishRequestDto requestDto) {
    WishResponseDto responseDto = service.createWish(member.getId(), requestDto);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @DeleteMapping
  public ResponseEntity<WishResponseDto> deleteWishByMemberId(@LoginMember Member member) {
    service.deleteAllWish(member.getId());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<WishResponseDto> deleteWishByProductId(@LoginMember Member member,
      @PathVariable("productId") Long productId) {
    service.deleteByProductId(member.getId(), productId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping
  public ResponseEntity<WishResponseDto> updateQuantity(@LoginMember Member member,
      @Valid @RequestBody WishRequestDto requestDto) {
    WishResponseDto responseDto = service.updateQuantity(member.getId(), requestDto);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }
}
