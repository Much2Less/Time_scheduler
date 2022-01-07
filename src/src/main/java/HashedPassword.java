import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashedPassword {

    private byte[] hashEncoded;
    private String hashString;

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

    public byte[] getHashEncoded() {
        return hashEncoded;
    }

    public void setHashEncoded(byte[] hashEncoded) {
        this.hashEncoded = hashEncoded;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }
}
