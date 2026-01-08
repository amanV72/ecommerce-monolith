package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.repositories.UserRepo;
import com.app.ecom.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    //private List<User> users = new ArrayList<>();
    //private Long nextId= 1L;

    private UserResponse mapToUserResponse(User user){
        UserResponse response= new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setUserRole(user.getUserRole());
        if(user.getAddress()!=null){
            AddressDTO addressDTO= new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }

        return response;

    }

    private void mapToUser(User user,UserRequest userRequest){

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress()!=null){
            Address address= new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }

    }


    public List<UserResponse> fetchUsers() {
        List<User> users= userRepo.findAll();

        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        //user.setId(nextId++);
        User user= new User();
        mapToUser(user,userRequest);
        userRepo.save(user);
    }

    public Optional<UserResponse> fetchOneUser(Long id){
        return userRepo.findById(id).map(this::mapToUserResponse);

    }

    public boolean updateUser(Long id,UserRequest userRequest) {
        return userRepo.findById(id)
                .map(existingUser->{
                    mapToUser(existingUser,userRequest);
                    userRepo.save(existingUser);
                    return true;
                }).orElse(false);

    }


}
