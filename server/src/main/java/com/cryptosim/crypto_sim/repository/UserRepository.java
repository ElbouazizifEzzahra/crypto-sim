package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Long , User> {
    Optional<User> findByEmail(String email);
}