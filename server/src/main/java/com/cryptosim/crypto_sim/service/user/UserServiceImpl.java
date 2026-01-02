package com.cryptosim.crypto_sim.service.user;

import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void addUser( UserDtoRequest userDtoRequest) {

    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void editUser() {

    }
}
