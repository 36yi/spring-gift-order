package gift.repository;

import gift.model.Wish;
import gift.model.User;
import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByUserAndProduct(User user, Product product);

    List<Wish> findAllByUser(User user);

    Page<Wish> findAllByUser(User user, Pageable pageable);

    void deleteByUserAndProduct(User user, Product product);
}