package lk.ijse.ecommerce_web_application;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.sql.DataSource;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the form data
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Validate user and get their role
        String userRole = getUserRole(username, password);

        if (userRole != null) {
            // If valid, create a session and set the user's role
            HttpSession session = req.getSession(true); // Creates a new session if one doesn't exist
            session.setAttribute("username", username);
            session.setAttribute("userRole", userRole);

            // Redirect to the dashboard
            resp.sendRedirect("index.jsp");
        } else {
            // If invalid, redirect to login page with error message
            req.setAttribute("errorMessage", "Invalid Username or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
    // Dummy validation function, replace with real logic
    private boolean isValidUser(String username, String password) {
        boolean isValid = false;

        // Query to validate user
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set query parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User found, credentials are valid
                    isValid = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }

        return isValid;
    }

    private String getUserRole(String username, String password) {
        String role = null;

        // Query to validate user and get their role
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set query parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Get the user's role
                    role = resultSet.getString("role");
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }

        return role;
    }
}