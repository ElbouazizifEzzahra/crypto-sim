package com.cryptosim.crypto_sim.service.user;

import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.dto.user.UserDtoResponse;
import com.cryptosim.crypto_sim.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
     public UserDtoResponse addUser(UserDtoRequest userRequestDTO);
     public void deleteUser(Long id);
     public void editUser(UserDtoRequest userRequestDTO);
     public List<UserDtoResponse> allUsers();
     public void changeUserPassword(Long userId,
                                    String currentPassword,
                                    String newPassword,
                                    String currentUsername);

}
