package gift.wish.service;

import gift.global.exception.ProductNotFoundException;
import gift.global.exception.WishAlreadyExistsException;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.dto.WishResponse;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository,
                       MemberRepository memberRepository,
                       ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllWishes(Member member) {
        List<Wish> wishes = wishRepository.findByMember(member);
        return wishes.stream()
                .map(Wish::getProduct)
                .map(ProductResponse::from)
                .toList();
    }

    public Page<WishResponse> getPagedWishes(Member member, Pageable pageable) {
        return wishRepository.findByMember(member, pageable)
                .map(WishResponse::from);
    }

    @Transactional
    public void addWish(Member member, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        if (wishRepository.existsByMemberAndProduct(member, product)) {
            throw new WishAlreadyExistsException(product);
        }

        wishRepository.save(new Wish(member, product));
    }

    @Transactional
    public void deleteWish(Member member, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        wishRepository.deleteByMemberAndProduct(member, product);
    }

    @Transactional
    public void updateWishQuantity(Member member, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (quantity <= 0) {
            wishRepository.delete(wish);
        } else {
            wish.updateQuantity(quantity);
        }
    }

}
