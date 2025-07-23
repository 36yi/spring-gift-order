package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.entity.Option;
import gift.entity.Product;
import gift.repository.option.OptionJpaRepository;
import gift.repository.product.ProductJpaRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OptionRepositoryTest {

  @Autowired
  EntityManager em;
  @Autowired
  private OptionJpaRepository optionRepository;
  @Autowired
  private ProductJpaRepository productRepository;

  @Test
  void 옵션저장() {
    Product product = new Product("product", 1000L, "https://naver.com");
    Product savedProduct = productRepository.save(product);
    Option option = new Option("option", 10, savedProduct);

    Option actual = optionRepository.save(option);

    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getName()).isEqualTo("option");
    assertThat(actual.getQuantity()).isEqualTo(10);
    assertThat(actual.getProduct()).isEqualTo(savedProduct);
  }

  @Test
  void 옵션조회() {
    Product product = new Product("product", 1000L, "https://naver.com");
    Product savedProduct = productRepository.save(product);
    Option option = new Option("option", 10, savedProduct);

    Option actual = optionRepository.save(option);
    product.addOption(actual);

    Option actual2 = productRepository.findById(savedProduct.getId()).orElseThrow()
        .getOptions().get(0);

    assertThat(actual.getId()).isEqualTo(actual2.getId());
    assertThat(actual.getName()).isEqualTo(actual2.getName());
    assertThat(actual.getQuantity()).isEqualTo(actual2.getQuantity());
    assertThat(actual.getProduct()).isEqualTo(actual2.getProduct());
  }

  @Test
  void 옵션전체삭제() {
    Product product = new Product("product", 1000L, "https://naver.com");
    Product savedProduct = productRepository.save(product);
    Option option1 = new Option("option", 10, savedProduct);
    Option option2 = new Option("option2", 10, savedProduct);

    Option actual1 = optionRepository.save(option1);
    Option actual2 = optionRepository.save(option2);
    product.addOption(actual1);
    product.addOption(actual2);

    optionRepository.deleteByProductId(savedProduct.getId());
    savedProduct.removeAllOption();

    List<Option> options = productRepository.findById(product.getId()).orElseThrow().getOptions();

    assertThat(options.size()).isEqualTo(0);
  }

  @Test
  void 옵션단건삭제() {
    Product product = new Product("product", 1000L, "https://naver.com");
    Product savedProduct = productRepository.save(product);
    Option option1 = new Option("option", 10, savedProduct);
    Option option2 = new Option("option2", 10, savedProduct);

    Option actual1 = optionRepository.save(option1);
    Option actual2 = optionRepository.save(option2);
    product.addOption(actual1);
    product.addOption(actual2);

    optionRepository.deleteByProductIdAndId(savedProduct.getId(), actual1.getId());
    savedProduct.removeOption(actual1);

    List<Option> options = productRepository.findById(product.getId()).orElseThrow().getOptions();

    assertThat(options.size()).isEqualTo(1);
    assertThat(options.getFirst().getId()).isEqualTo(actual2.getId());
    assertThat(options.getFirst().getName()).isEqualTo(actual2.getName());
    assertThat(options.getFirst().getQuantity()).isEqualTo(actual2.getQuantity());
    assertThat(options.getFirst().getProduct()).isEqualTo(actual2.getProduct());
  }
}
