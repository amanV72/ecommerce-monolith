package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repositories.CartItemRepo;
import com.app.ecom.repositories.ProductRepo;
import com.app.ecom.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    public void cartItemToCartItemResponse(CartItem cartItem) {

    }

    public boolean addToCart(String userId, CartItemRequest request) {
        //Look for Product
        Optional<Product> productOpt = productRepo.findById(request.getProductId());
        if (productOpt.isEmpty()) return false;
        Product product = productOpt.get();
        if (product.getStockQuantity() < request.getQuantity()) return false;

        //Look for User
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();

        CartItem existingItem = cartItemRepo.findByUserAndProduct(user, product);

        if (existingItem != null) {
            //Update the quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setPrice(product.getPrice());
            existingItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepo.save(existingItem);
        } else {
            //Create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepo.save(cartItem);

        }
        return true;
    }


    public boolean deleteItemFromCart(String userId, Long productId) {

        Optional<Product> productOpt = productRepo.findById(productId);


        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if (productOpt.isPresent() && userOpt.isPresent()) {
            cartItemRepo.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }


        return false;

    }

    public List<CartItem> getCart(String userId) {

        return userRepo.findById(Long.valueOf(userId))
                .map(cartItemRepo::findByUser)
                .orElseGet(List::of);

    }

    public void clearCart(String userId) {
        userRepo.findById(Long.valueOf(userId)).ifPresent(cartItemRepo::deleteByUser);
    }
}
