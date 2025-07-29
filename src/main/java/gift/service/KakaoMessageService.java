package gift.service;

import gift.model.Order;
import gift.model.User;
import gift.service.external.KakaoApi;
import org.springframework.stereotype.Service;

@Service
public class KakaoMessageService {

    private final KakaoApi kakaoApi;

    public KakaoMessageService(KakaoApi kakaoApi) {
        this.kakaoApi = kakaoApi;
    }

    public void sendOrderMessage(Order order, String accessToken) {
        String message = String.format(
                "주문이 완료되었습니다!\n상품: %s\n옵션: %s\n수량: %d\n메시지: %s",
                order.getProductOption().getProduct().getName(),
                order.getProductOption().getName(),
                order.getQuantity(),
                order.getMessage()
        );
        kakaoApi.sendToMe(accessToken, message);
    }
}