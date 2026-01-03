package com.cryptosim.crypto_sim.dto.user;

import lombok.Data;

@Data
public class PasswordChangeDto {
    private String currentPassword;
    private String newPassword;
}