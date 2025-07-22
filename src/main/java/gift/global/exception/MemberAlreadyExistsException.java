package gift.global.exception;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(Long id) {
        super("이미 사용 중인 회원입니다. ID: " + id);
    }

    public MemberAlreadyExistsException(String email) {
        super("이미 가입된 이메일입니다. Email: " + email);
    }
}
