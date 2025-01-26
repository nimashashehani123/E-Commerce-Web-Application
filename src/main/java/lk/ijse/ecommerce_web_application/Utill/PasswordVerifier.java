package lk.ijse.ecommerce_web_application.Utill;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerifier {
    // Method to verify the password against the hashed password
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword); // Returns true if the passwords match
    }
}
