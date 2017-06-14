package com.eat.utils.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface PasswordEncryptor {

    default String encryptPassword(String password, BCryptPasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    default boolean isMatches(String password, String encryptedPassword, BCryptPasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

}