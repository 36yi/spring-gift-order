package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

  Optional<WishList> findByMemberAndProduct(Member member, Product product);

  List<WishList> findAllByMember(Member member, Sort sort);

  Page<WishList> findAllByMember(Member member, Pageable pageable);

  void deleteByMemberAndProduct(Member member, Product product);

  void deleteAllByMember(Member member);
}
