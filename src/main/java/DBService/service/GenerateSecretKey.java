package DBService.service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

/**
 * Created by Nik on 27.06.2017.
 */
public class GenerateSecretKey {
    public static String generate(String visitor, String filmName, String cinemaName) {
        String secretKey = "";
        try {
            String strForEncript = visitor + "_" + filmName + "_" + cinemaName;
            KeyGenerator generator = KeyGenerator.getInstance ("AES");
            generator.init( new SecureRandom(strForEncript.getBytes()));
            SecretKey key = generator.generateKey();
            String _key = key.toString();
            secretKey = _key.substring(key.toString().indexOf("@") + 1, key.toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return secretKey;
        }
    }
}
