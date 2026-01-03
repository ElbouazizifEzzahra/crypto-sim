package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Orders;
import com.cryptosim.crypto_sim.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Long, Orders> {
    List<Orders> findByStatutOrder(Status status);

    // Find orders for a specific user
    List<Orders> findByUserId(Long userId);

    void save(Orders order);
}
