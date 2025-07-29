package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.jwt.JwtTokenProvider;
import gift.model.User;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request,
                                                        @LoginUser User user,
                                                        @RequestHeader("Authorization") String jwtToken) {
        String pureToken = jwtToken.replace("Bearer ", "");
        String accessToken = jwtTokenProvider.getKakaoAccessTokenFromToken(pureToken);
        OrderResponseDTO response = orderService.createOrder(request, user, accessToken);
        return ResponseEntity.status(201).body(response);
    }
}