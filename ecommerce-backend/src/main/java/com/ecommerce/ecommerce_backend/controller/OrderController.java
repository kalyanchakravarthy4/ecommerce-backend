package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.CreateOrderRequest;
import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create order (from Payment page)
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest req,
                                             Principal principal) {
        String userEmail = principal.getName();
        Order order = orderService.createOrder(req, userEmail);
        return ResponseEntity.ok(order);
    }

    // Get logged in user's orders
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        String userEmail = principal.getName();
        List<Order> orders = orderService.getOrdersForUser(userEmail);
        return ResponseEntity.ok(orders);
    }

    // Cancel order
    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable("id") String id,
                                            Principal principal) {
        String userEmail = principal.getName();
        orderService.cancelOrder(id, userEmail);
        return ResponseEntity.ok().build();
    }
}
