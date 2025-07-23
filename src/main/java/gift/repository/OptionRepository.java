package gift.repository;

import gift.domain.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

  List<Option> findByProductId(Long productId);

  Optional<Option> findByProductIdAndName(Long productId, String name);
}
