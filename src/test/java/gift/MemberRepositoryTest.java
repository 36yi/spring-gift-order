package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.entity.Member;
import gift.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MemberRepositoryTest {

  @Autowired
  private MemberJpaRepository repository;

  @Test
  void 회원저장() {
    Member member = new Member("aaa@naver.com", "qweqwe");
    Member actual = repository.save(member);

    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getEmail()).isEqualTo("aaa@naver.com");
    assertThat(actual.getPassword()).isEqualTo("qweqwe");
  }

  @Test
  void 회원아이디_조회() {
    Member member = new Member("aaa@naver.com", "qweqwe");
    Member actual = repository.save(member);

    Member actual2 = repository.findById(actual.getId()).orElseThrow();

    assertThat(actual.getId()).isEqualTo(actual2.getId());
    assertThat(actual.getEmail()).isEqualTo(actual2.getEmail());
    assertThat(actual.getPassword()).isEqualTo(actual2.getPassword());
  }

  @Test
  void 회원이메일_조회() {
    Member member = new Member("aaa@naver.com", "qweqwe");
    Member actual = repository.save(member);

    Member actual2 = repository.findByEmail(actual.getEmail()).get();

    assertThat(actual.getId()).isEqualTo(actual2.getId());
    assertThat(actual.getEmail()).isEqualTo(actual2.getEmail());
    assertThat(actual.getPassword()).isEqualTo(actual2.getPassword());
  }

  @Test
  void 회원수정() {
    Member member = new Member("aaa@naver.com", "qweqwe");
    Member actual = repository.save(member);

    Member actual2 = repository.findByEmail(actual.getEmail()).orElseThrow();
    actual2.update("update@naver.com", "qweqwe123");

    Member updatedMember = repository.findById(actual.getId()).orElseThrow();
    assertThat(updatedMember.getEmail()).isEqualTo("update@naver.com");
    assertThat(updatedMember.getPassword()).isEqualTo("qweqwe123");
  }

  @Test
  void 회원삭제() {
    Member member = new Member("aaa@naver.com", "qweqwe");
    Member actual = repository.save(member);

    repository.deleteById(actual.getId());

    boolean present = repository.findById(actual.getId()).isPresent();
    assertThat(present).isFalse();
  }
}
