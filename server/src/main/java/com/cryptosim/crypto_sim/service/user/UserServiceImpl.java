package com.cryptosim.crypto_sim.service.user;

import com.cryptosim.crypto_sim.dto.user.UserDtoRequest;
import com.cryptosim.crypto_sim.dto.user.UserDtoResponse;
import com.cryptosim.crypto_sim.dto.user.UserMapper;
import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final WalletRepository walletRepository;

    @Override
    public UserDtoResponse addUser(@Valid UserDtoRequest userRequestDTO) {
        User user =userMapper.toEntity(userRequestDTO);
        if(!userRepository.existsByEmail(user.getEmail())){
            user.setPassword(encoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            Wallet wallet = new Wallet();
            wallet.setUser(savedUser );
            wallet.setUsdBalance(new BigDecimal("10000.00"));
            wallet.setBtcLocked(BigDecimal.ZERO);
            wallet.setEthLocked(BigDecimal.ZERO);
            wallet.setSolLocked(BigDecimal.ZERO);
            walletRepository.save(wallet);

            return  userMapper.toDTO(savedUser);}

        else
            return null;

    }
    
    @Override
    public com.cryptosim.crypto_sim.model.User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void editUser(@Valid UserDtoRequest userRequestDTO) {
        if (userRequestDTO == null) {
            return;
        }
        if (userRequestDTO.getId() == null) {
            throw new IllegalArgumentException("User ID is required for updating.");
        }
        
        User existingUser = userRepository.findById(userRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRequestDTO.getEmail() != null 
                && !existingUser.getEmail().equals(userRequestDTO.getEmail())
                && userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRequestDTO.getFirstName() != null) {
            existingUser.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            existingUser.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getEmail() != null) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }

        userRepository.save(existingUser);
    }

    @Override
    public List<UserDtoResponse> allUsers() {
        List< User> users =userRepository.findAll();

        return userMapper.toDTO(users) ;
    }

    @Override
    public void changeUserPassword(Long userId,
                                   String currentPassword,
                                   String newPassword,
                                   String currentUsername) {

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        } else if (currentUsername != null) {
            user = userRepository.findByEmail(currentUsername).orElse(null);
        }

        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Mot de passe actuel incorrect");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
}
