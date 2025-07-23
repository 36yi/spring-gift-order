package gift.option;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionRepositoryTest {

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("특정 상품 ID로 옵션 목록 조회")
  void testFindByProductId() {
    Product product = productRepository.save(new Product("테스트 상품", 10000, "test.jpg"));
    Option option1 = new Option("옵션1", 100, product);
    Option option2 = new Option("옵션2", 200, product);
    optionRepository.save(option1);
    optionRepository.save(option2);

    List<Option> options = optionRepository.findByProductId(product.getId());

    assertThat(options).hasSize(2)
        .extracting(Option::getName)
        .containsExactly("옵션1", "옵션2");
  }

  @Test
  @DisplayName("상품 ID와 옵션 이름으로 단일 옵션 조회")
  void testFindByProductIdAndName() {
    Product product = productRepository.save(new Product("테스트 상품", 10000, "test.jpg"));
    Option option = new Option("테스트 옵션", 1, product);
    optionRepository.save(option);

    Optional<Option> found = optionRepository.findByProductIdAndName(product.getId(), "테스트 옵션");

    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("테스트 옵션");
    assertThat(found.get().getQuantity()).isEqualTo(1);
  }
}

