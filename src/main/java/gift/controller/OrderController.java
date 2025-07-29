package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.User;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request,
                                                        @LoginUser User user) {
        OrderResponseDTO response = orderService.createOrder(request, user);
        return ResponseEntity.status(201).body(response);
    }
}