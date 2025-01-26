package lk.ijse.ecommerce_web_application.Utill;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncrypt {
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        return hashedPassword;
    }
}
