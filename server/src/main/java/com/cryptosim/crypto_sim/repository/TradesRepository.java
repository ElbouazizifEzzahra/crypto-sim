package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Orders;
import com.cryptosim.crypto_sim.model.Trades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradesRepository extends JpaRepository< Trades,Long> {

}
