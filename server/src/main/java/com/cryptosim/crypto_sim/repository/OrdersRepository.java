package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Long, Orders> {
}
