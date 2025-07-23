package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.member.MemberJpaRepository;
import gift.repository.product.ProductJpaRepository;
import gift.repository.wish.WishJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class WishRepositoryTest {

  @Autowired
  private WishJpaRepository wishRepository;

  @Autowired
  private MemberJpaRepository memberRepository;

  @Autowired
  private ProductJpaRepository productRepository;


  @Test
  void 위시리스트저장() {
    Member member = new Member("member@naver.com", "qweqwe");
    Product product = new Product("product", 100L, "https://naver.com");
    Wish wish = new Wish(member, product, 3L);

    Wish actual = wishRepository.save(wish);

    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getProduct()).isEqualTo(product);
    assertThat(actual.getQuantity()).isEqualTo(3L);
  }

  @Test
  void 위시리스트_조회() {
    Member member = new Member("member@naver.com", "qweqwe");
    Product product = new Product("product", 100L, "https://naver.com");
    Wish wish = new Wish(member, product, 3L);
    Wish actual = wishRepository.save(wish);

    Wish actual2 = wishRepository.findById(actual.getId()).orElseThrow();

    assertThat(actual2.getId()).isEqualTo(actual.getId());
    assertThat(actual2.getMember()).isEqualTo(actual.getMember());
    assertThat(actual2.getProduct()).isEqualTo(actual.getProduct());
    assertThat(actual2.getQuantity()).isEqualTo(actual.getQuantity());
  }

  @Test
  void 위시리스트_페이지네이션테스트() {
    Member member = new Member("member@naver.com", "qweqwe");
    memberRepository.save(member);
    for (int i = 1; i <= 21; i++) {
      Product product = new Product("product", 100L, "https://naver.com");
      productRepository.save(product);
      Wish wish = new Wish(member, product, 3L);
      wishRepository.save(wish);
    }
    Pageable pageable = PageRequest.of(0, 5);

    Page<Wish> result = wishRepository.findByMemberId(member.getId(), pageable);

    assertThat(result.getContent().size()).isEqualTo(5);
    assertThat(result.getTotalElements()).isEqualTo(21);
    assertThat(result.getTotalPages()).isEqualTo(5);
  }

  @Test
  void 위시리스트_수정() {
    Member member = new Member("member@naver.com", "qweqwe");
    Product product = new Product("product", 100L, "https://naver.com");
    Wish wish = new Wish(member, product, 3L);
    Wish actual = wishRepository.save(wish);

    Wish actual2 = wishRepository.findById(actual.getId()).orElseThrow();
    actual2.updateQuantity(4L);

    Wish updatedWish = wishRepository.findById(actual.getId()).orElseThrow();

    assertThat(updatedWish.getQuantity()).isEqualTo(actual.getQuantity());
  }

  @Test
  void 위시리스트_삭제() {
    Member member = new Member("member@naver.com", "qweqwe");
    Product product = new Product("product", 100L, "https://naver.com");
    Wish wish = new Wish(member, product, 3L);
    Wish actual = wishRepository.save(wish);

    wishRepository.deleteById(actual.getId());

    boolean present = wishRepository.findById(actual.getId()).isPresent();
    assertThat(present).isFalse();
  }

}
