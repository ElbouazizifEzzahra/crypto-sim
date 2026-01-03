package com.cryptosim.crypto_sim.dto.user;

import com.cryptosim.crypto_sim.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {
    private UserDtoRequest userDtoRequest;
    private UserDtoResponse userDtoResponse;
    private User user;
    public User toEntity(UserDtoRequest userDtoRequest){
       user.setEmail(userDtoRequest.getEmail());
       user.setPassword(userDtoRequest.getPassword());
       user.setFirstName(userDtoRequest.getFirstName());
       user.setLastName(userDtoRequest.getLastName());
       user.setPassword(userDtoRequest.getPassword());
     return user;

    }
    public UserDtoResponse toDto( User user){
        userDtoResponse.setId(user.getId());
        userDtoResponse.setFirstName(user.getFirstName());
        userDtoResponse.setLastName(user.getLastName());
        userDtoResponse.setEmail(user.getEmail());

        return userDtoResponse;
    }


}
