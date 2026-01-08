package com.app.ecom.controller;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {


    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
//        Optional<User> user= userService.fetchOneUser(id);
//        if(user==null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(user);

        return userService.fetchOneUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest user) {
        System.out.println("Received list is: "+user);
        userService.addUser(user);
        return ResponseEntity.ok("User added!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest user) {
        boolean isUpdated = userService.updateUser(id, user);
        return isUpdated ? ResponseEntity.ok("Updated") : ResponseEntity.notFound().build();
    }
}
