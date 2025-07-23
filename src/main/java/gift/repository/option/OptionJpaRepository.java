package gift.repository.option;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionJpaRepository extends JpaRepository<Option, Long> {

  void deleteByProductId(Long productId);

  void deleteByProductIdAndId(Long productId, Long id);
}
