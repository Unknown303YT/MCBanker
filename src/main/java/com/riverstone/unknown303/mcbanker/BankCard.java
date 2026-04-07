package com.riverstone.unknown303.mcbanker;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class BankCard {
    private final String username;
    private String cardNumSalt = null;

    private final SecureRandom CRYPTO;

    public BankCard(String username) {
        this.username = username;
        CRYPTO = new SecureRandom();
    }

    public String getUsername() {
        return username;
    }

    public char[] getCardNumSalt() {
        return cardNumSalt.toCharArray();
    }

    public void setCardNumSalt(char[] cardNumSalt) {
        this.cardNumSalt = new String(cardNumSalt);
    }

    public long getCardNumber() {
        try {
            if (cardNumSalt == null)
                generateCardNumSalt();

            int userId = username.hashCode();

            String input = userId + ":" + cardNumSalt;

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(input.getBytes(StandardCharsets.UTF_8));

            long raw = ByteBuffer.wrap(hash).getLong();
            long unsigned = raw & 0x7FFFFFFFFFFFFFFFL;

            return unsigned % 1_000_000_000_000L;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyCard(long cardNumber) {
        if (cardNumSalt == null)
            return false;

        return MessageDigest.isEqual(
                ByteBuffer.allocate(Long.BYTES).putLong(cardNumber).array(),
                ByteBuffer.allocate(Long.BYTES).putLong(getCardNumber()).array()
        );
    }

    private void generateCardNumSalt() {
        byte[] bytes = new byte[32];
        CRYPTO.nextBytes(bytes);
        cardNumSalt = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
