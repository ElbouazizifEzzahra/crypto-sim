package com.cryptosim.crypto_sim.service.user;

import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.model.User;

public interface UserService {
     void addUser(UserDtoRequest userDtoRequest);
     void deleteUser();
     void editUser();

}
