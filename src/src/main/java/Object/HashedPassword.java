package Object;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class for hashing a string of characters, preferably a password
 */
public class HashedPassword {

    private byte[] hashEncoded;
    private String hashString;

    /**
     * Creates a new HashedPassword Object which stores
     * the encoded hash of a string in a byte array and builds a fully usable hashed string
     * Hashing Method: SHA-256
      * @param password string to be hashed
     */
    public HashedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        this.hashEncoded = encodedHash;

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (int i = 0; i < encodedHash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedHash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        this.hashString = hexString.toString();
    }

    /**
     * Returns the encoded string as a byte array
     */
    public byte[] getHashEncoded() {
        return hashEncoded;
    }

    /**
     * Returns the hashed string
     */
    public String getHashString() {
        return hashString;
    }

}
