package com.example.loginscreen.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private Date createDate = new Date();
    private String name; //username
    private String password;
    private String encryptedPassword;

    public void setEncryptedPasswordCustom(String password) {
        // Encrypt the password before setting it
        this.encryptedPassword = hashPassword(password);
    }

    //Sifreleme algoritması
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed.");
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}
