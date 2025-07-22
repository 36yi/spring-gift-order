package gift.global.exception;

public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),

    // 상품 관련
    PRODUCT_NOT_FOUND("해당 상품을 찾을 수 없습니다."),
    INVALID_PRODUCT("유효하지 않은 상품입니다."),
    NAME_BLANK("상품 이름은 비어 있을 수 없습니다."),
    NAME_TOO_LONG("상품 이름은 최대 15자까지 입력할 수 있습니다."),
    NAME_INVALID_CHARACTERS("상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다."),
    NAME_CONTAINS_KAKAO("\"카카오\"가 포함된 상품 이름은 사용할 수 없습니다."),
    PRICE_NEGATIVE("가격은 0 이상이어야 합니다."),
    IMGURL_BLANK("이미지 URL은 비어 있을 수 없습니다."),

    // 회원 관련
    INVALID_EMAIL("이메일 형식이 올바르지 않습니다."),
    PASSWORD_BLANK("비밀번호는 비어 있을 수 없습니다."),
    PASSWORD_TOO_SHORT("비밀번호는 최소 8자 이상이어야 합니다."),
    EMAIL_ALREADY_EXISTS("이미 사용중인 이메일입니다"),
    MEMBER_ALREADY_EXISTS("이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    LOGIN_FAILED("이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),

    //위시관련
    WISH_ALREADY_EXISTS("이미 위시리스트에 있습니다"),

    // 옵션 관련
    OPTION_NAME_INVALID("옵션 이름 형식이 올바르지 않습니다."),
    OPTION_QUANTITY_INVALID("옵션 수량은 1 이상 1억 미만이어야 합니다."),
    OPTION_ALREADY_EXISTS("이미 존재하는 옵션입니다."),
    OPTION_NOT_FOUND("해당 옵션을 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
