package gift.repository.product;

import gift.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

  @Override
  Page<Product> findAll(Pageable pageable);
}
