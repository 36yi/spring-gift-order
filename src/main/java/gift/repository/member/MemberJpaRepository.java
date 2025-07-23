package gift.repository.member;

import gift.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
}
