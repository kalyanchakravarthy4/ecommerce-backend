package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.dto.CreateOrderRequest;
import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.OrderItem;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.repository.OrderRepository;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(CreateOrderRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.Item itemReq : req.getItems()) {
            Optional<Product> productOpt = productRepository.findById(itemReq.getProductId());
            if (!productOpt.isPresent()) {
                throw new RuntimeException("Product not found: " + itemReq.getProductId());
            }
            Product product = productOpt.get();

            // assuming Product.getPrice() returns BigDecimal
            BigDecimal price = product.getPrice();

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(price);
            item.setQuantity(itemReq.getQuantity());

            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(lineTotal);

            items.add(item);
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setItems(items);
        order.setTotalAmount(total);
        order.setDeliveryAddress(req.getDeliveryAddress());
        order.setPaymentMethod(req.getPaymentMethod());
        order.setStatus("PLACED");
        order.setCreatedAt(Instant.now());

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public void cancelOrder(String orderId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUserId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to cancel this order");
        }

        if (!"CANCELLED".equalsIgnoreCase(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
        }
    }
}
