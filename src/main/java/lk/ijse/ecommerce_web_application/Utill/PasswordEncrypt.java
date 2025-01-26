package lk.ijse.ecommerce_web_application.Utill;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncrypt {
    // Method to hash the password with bcrypt
    public static String hashPassword(String password) {
        // Generates a salt with the default bcrypt strength (log_rounds = 10)
        String salt = BCrypt.gensalt(12); // 12 is a good default value for bcrypt strength
        String hashedPassword = BCrypt.hashpw(password, salt); // Hash the password with the salt
        return hashedPassword; // Return the hashed password
    }
}