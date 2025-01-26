package lk.ijse.ecommerce_web_application;

import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.ecommerce_web_application.Utill.PasswordEncrypt;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;

@WebServlet("/AdminRegisterServlet")
public class AdminRegisterServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;
    private static final String ADMIN_CONFIRMATION_CODE = "SECRET123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmationCode = request.getParameter("confirmationCode");

        if (!ADMIN_CONFIRMATION_CODE.equals(confirmationCode)) {
            request.setAttribute("error", "Invalid confirmation code!");
            request.getRequestDispatcher("admin_register.jsp").forward(request, response);
            return;
        }

        String hashedPassword = PasswordEncrypt.hashPassword(password);

        try (Connection conn = dataSource.getConnection()) {
            String query = "INSERT INTO users (name, email, username, password, role, status) VALUES (?, ?, ?, ?, 'admin', 'active')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.setString(4, hashedPassword);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                request.setAttribute("message", "Admin registered successfully!");
                request.getRequestDispatcher("admin_register.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to register admin. Please try again.");
                request.getRequestDispatcher("admin_register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("admin_register.jsp").forward(request, response);
        }
    }
}

