package gift.service;

import gift.jwt.JwtTokenProvider;
import gift.model.Product;
import gift.model.User;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public WishService(ProductRepository productRepository, UserRepository userRepository, WishRepository wishRepository, JwtTokenProvider jwtTokenProvider) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public void addWish(Long userId, Long productId) {
        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);

        Wish wish = new Wish(user, product, 1L);

        wishRepository.save(wish);
    }

    public void deleteWish(Long userId, Long productId) {
        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);

        wishRepository.deleteByUserAndProduct(user, product);
    }

    public List<Wish> getAllWish(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return wishRepository.findAllByUser(user);
    }
    public void increaseWish(Long userId, Long productId) {
        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);

        Wish wish = wishRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalArgumentException("해당 찜 기록이 없습니다."));

        wish.setCount(wish.getCount() + 1);
        wishRepository.save(wish);
    }

    public void decreaseWish(Long userId, Long productId) {
        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(productId);

        Wish wish = wishRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalArgumentException("해당 찜 기록이 없습니다."));

        if (wish.getCount() > 0) {
            wish.setCount(wish.getCount() - 1);
            wishRepository.save(wish);
        }
    }
    public Page<Wish> getPagedWishList(Long userId, Pageable pageable) {
        User user = userRepository.getReferenceById(userId);
        return wishRepository.findAllByUser(user, pageable);
    }
}