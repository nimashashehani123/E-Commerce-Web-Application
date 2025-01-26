package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.ijse.ecommerce_web_application.Utill.PasswordEncrypt;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String repassword = req.getParameter("repassword");

            if (!password.equals(repassword)) {
                req.setAttribute("error", "Passwords do not match");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

            String hashedPassword = PasswordEncrypt.hashPassword(password);

            if (isUsernameTaken(username)) {
                req.setAttribute("error", "Username already exists");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

            boolean isUserRegistered = registerUser(name, email, username, hashedPassword);

            if (isUserRegistered) {
                resp.sendRedirect("login.jsp");
            } else {
                req.setAttribute("error", "Registration failed. Please try again.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }
        }else if ("update".equals(action)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");

            if (password != null && !password.equals(confirmPassword)) {
                req.setAttribute("error", "Passwords do not match!");
                req.getRequestDispatcher("updateAccount.jsp").forward(req, resp);
                return;
            }

            String hashedPassword = null;
            if (password != null && !password.isEmpty()) {
               hashedPassword = PasswordEncrypt.hashPassword(password);
            }

            try (Connection conn = dataSource.getConnection()) {
                String sql = "UPDATE users SET name = ?, email = ?" +
                        (hashedPassword != null ? ", password = ?" : "") +
                        " WHERE user_id = ?";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, email);

                if (hashedPassword != null) {
                    stmt.setString(3, hashedPassword);
                    stmt.setInt(4, getCurrentUserId(req));
                } else {
                    stmt.setInt(3, getCurrentUserId(req));
                }

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    req.setAttribute("message", "Account updated successfully!");
                } else {
                    req.setAttribute("error", "Failed to update account details.");
                }

                resp.sendRedirect("user");
            } catch (Exception e) {
                throw new ServletException("Error updating account details", e);
            }
        }
    }

    private int getCurrentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null && session.getAttribute("user_id") != null) ?
                (int) session.getAttribute("user_id") : -1;
    }




        private boolean isUsernameTaken (String username){
            String sql = "SELECT 1 FROM users WHERE username = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }


        private boolean registerUser (String name, String email, String username, String hashedPassword){
            String sql = "INSERT INTO users (name, email, username, password) VALUES (?, ?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, hashedPassword);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

