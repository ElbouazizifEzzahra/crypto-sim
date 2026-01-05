package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    public void deleteById(Long id);
     User findUserById(Long id);
    List<User>findAll();

}