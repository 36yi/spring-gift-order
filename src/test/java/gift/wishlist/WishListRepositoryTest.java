package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Role;
import gift.domain.WishList;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class WishListRepositoryTest {

  @Autowired
  private WishListRepository wishListRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    wishListRepository.deleteAll();
    productRepository.deleteAll();
    memberRepository.deleteAll();
  }

  @Test
  @DisplayName("위시리스트 저장 및 조회 테스트")
  void saveAndFindTest() {
    Member member = memberRepository.save(new Member(null, "sample@naver.com", "password", Role.USER));
    Product product1 = productRepository.save(new Product(null, "상품1", 1000, "image1.jpg"));
    Product product2 = productRepository.save(new Product(null, "상품2", 2000, "image2.jpg"));

    WishList wish1 = wishListRepository.save(new WishList(null, member, product1, 111));
    WishList wish2 = wishListRepository.save(new WishList(null, member, product2, 222));

    List<WishList> results = wishListRepository.findAllByMember(member, Sort.by(Sort.Direction.ASC, "product.id"));

    assertAll(
        () -> assertThat(results).hasSize(2),
        () -> assertThat(results.get(0).getProduct().getName()).isEqualTo("상품1"),
        () -> assertThat(results.get(0).getQuantity()).isEqualTo(111),
        () -> assertThat(results.get(1).getProduct().getName()).isEqualTo("상품2"),
        () -> assertThat(results.get(1).getQuantity()).isEqualTo(222)
    );
  }

  @Test
  @DisplayName("특정 회원과 상품으로 위시리스트 조회")
  void findByMemberAndProductTest() {
    Member member = memberRepository.save(new Member(null, "sample@naver.com", "password", Role.USER));
    Product product = productRepository.save(new Product(null, "상품3", 3000, "image3.jpg"));

    WishList wish = wishListRepository.save(new WishList(null, member, product, 333));

    Optional<WishList> found = wishListRepository.findByMemberAndProduct(member, product);

    assertAll(
        () -> assertThat(found).isPresent(),
        () -> assertThat(found.get().getQuantity()).isEqualTo(333)
    );
  }
}
