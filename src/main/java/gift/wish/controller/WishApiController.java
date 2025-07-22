package gift.wish.controller;

import gift.wish.dto.WishRequest;
import gift.member.entity.Member;
import gift.global.resolver.LoginMember;
import gift.wish.dto.WishResponse;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {

    private final WishService wishService;

    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Page<WishResponse>> getPagedWishes(
            @LoginMember Member member,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Page<WishResponse> wishes = wishService.getPagedWishes(member, pageable);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@LoginMember Member member, @Valid @RequestBody WishRequest request) {
        wishService.addWish(member, request.productId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateQuantity(
            @LoginMember Member member,
            @Valid @RequestBody WishRequest request
    ) {
        wishService.updateWishQuantity(member, request.productId(), request.quantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Member member, @PathVariable("productId") Long productId) {
        wishService.deleteWish(member, productId);
        return ResponseEntity.noContent().build();
    }
}