package com.cryptosim.crypto_sim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
