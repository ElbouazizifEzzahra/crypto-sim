
package com.cryptosim.crypto_sim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Timestamp created_at;
    @OneToMany
    private List<Wallet> wallets;
    @OneToMany
    private List<Transaction> orders;


}