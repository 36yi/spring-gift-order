package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.jwt.JwtTokenProvider;
import gift.jwt.JwtUtils;
import gift.model.User;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtils jwtUtils;

    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.orderService = orderService;
        this.jwtUtils = new JwtUtils(jwtTokenProvider);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request,
                                                        @LoginUser User user,
                                                        @RequestHeader(value = "Authorization", required = false) Optional<String> jwtTokenOptional) {
        String accessToken = null;
        if (jwtTokenOptional.isPresent()) {
            String jwtToken = jwtTokenOptional.get();
            String pureToken = jwtUtils.extractPureToken(jwtToken);
            if (pureToken != null) {
                accessToken = jwtUtils.getKakaoAccessTokenFromPureToken(pureToken);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        OrderResponseDTO response = orderService.createOrder(request, user, accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);


    }
}