package com.cryptosim.crypto_sim.dto.user;

import com.cryptosim.crypto_sim.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class UserMapper {



    public User toEntity(UserDtoRequest userDtoRequest){
        User user = new User();
        user.setId(userDtoRequest.getId());
       user.setEmail(userDtoRequest.getEmail());
       user.setFirstName(userDtoRequest.getFirstName());
       user.setLastName(userDtoRequest.getLastName());
       user.setPassword(userDtoRequest.getPassword());
     return user;

    }
    public UserDtoResponse toDTO( User user){
        UserDtoResponse userDtoResponse=new UserDtoResponse();
        userDtoResponse.setId(user.getId());
        userDtoResponse.setFirstName(user.getFirstName());
        userDtoResponse.setLastName(user.getLastName());
        userDtoResponse.setEmail(user.getEmail());

        return userDtoResponse;
    }

    public List <UserDtoResponse > toDTO(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
