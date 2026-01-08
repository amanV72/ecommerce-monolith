package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getFromCart(@RequestHeader("X-User-ID") String userId) {

        return ResponseEntity.ok(cartService.getCart(userId));

    }

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request) {

        boolean isAdded = cartService.addToCart(userId, request);

        if (!isAdded) return ResponseEntity.badRequest().body("Product Out of Stock or User Not Found!");

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteFromCart(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable Long productId) {
        boolean isDeleted = cartService.deleteItemFromCart(userId, productId);

        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();

    }
}
