package com.app.ecom.service;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.*;
import com.app.ecom.repositories.OrderRepo;
import com.app.ecom.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final CartService cartService;
    private final UserRepo userRepo;


    public Optional<OrderResponse> createOrder(String userId) {
        /// Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        /// Validate for User
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if (userId.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();

        /// Calculate Total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /// Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        /// Clear the cart
        cartService.clearCart(userId);

        return Optional.of(orderToOrderResponse(savedOrder));

    }

    private OrderResponse orderToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream().map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).toList(),
                order.getCreatedAt()
        );
    }
}
