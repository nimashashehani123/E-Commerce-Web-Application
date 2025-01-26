package lk.ijse.ecommerce_web_application.Utill;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerifier {
    public static boolean verifyPassword(String password,String hashedPassword) {
        return BCrypt.checkpw(password,hashedPassword);
    }
}
