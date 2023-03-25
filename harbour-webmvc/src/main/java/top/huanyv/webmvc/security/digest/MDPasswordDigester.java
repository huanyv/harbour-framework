package top.huanyv.webmvc.security.digest;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author huanyv
 * @date 2023/3/23 17:23
 */
public class MDPasswordDigester implements PasswordDigester {

    private final String algorithm;

    public MDPasswordDigester(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String digest(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(this.algorithm);
            md.update(password.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(algorithm + "not found.");
    }

    @Override
    public boolean match(String password, String digest) {
        return Objects.equals(digest(password), digest);
    }

}
