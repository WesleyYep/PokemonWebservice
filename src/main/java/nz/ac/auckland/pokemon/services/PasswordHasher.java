package nz.ac.auckland.pokemon.services;

import java.security.MessageDigest;

/**
 * Helper class for hashing a password so that it is not stored or sent in plain text
 * Uses the SHA-256 hash algorithm
 *
 * Created by Wesley on 27/09/2015.
 */
public class PasswordHasher {
    private static final String HASH_ALGORITHM = "SHA-256";

    public static String passwordHash(String password) {
        try{
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
