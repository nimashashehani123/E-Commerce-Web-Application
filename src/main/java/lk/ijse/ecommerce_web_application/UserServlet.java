package lk.ijse.ecommerce_web_application;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.ijse.ecommerce_web_application.Utill.PasswordVerifier;

import javax.sql.DataSource;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Check if a session exists
        HttpSession session = request.getSession(false);


        if (session == null) {
            // If no session exists, redirect to the login page
            response.sendRedirect("login.jsp");
        } else {
            String action = request.getParameter("action");
            if ("updateAccount".equals(action)) {
                Integer userId = (session != null && session.getAttribute("user_id") != null)
                        ? (Integer) session.getAttribute("user_id")
                        : null;

                if (userId == null) {
                    // Redirect to login page if user is not logged in
                    response.sendRedirect("login.jsp");
                    return;
                }

                String name = null;
                String email = null;

                // Fetch user details from the database
                try (Connection conn = dataSource.getConnection()) {
                    String sql = "SELECT name, email FROM users WHERE user_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        name = rs.getString("name");
                        email = rs.getString("email");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error loading user details.");
                }

                // Set attributes for forwarding to JSP
                request.setAttribute("name", name);
                request.setAttribute("email", email);

                // Forward to updateAccount.jsp
                request.getRequestDispatcher("updateAccount.jsp").forward(request, response);
            } else {

                // Safely retrieve session attributes after confirming the session exists
                String userRole = (String) session.getAttribute("userRole");
                Integer userId = (Integer) session.getAttribute("user_id");
                String userName = (String) session.getAttribute("user_name");

                // If user attributes are missing, redirect to the login page as a precaution
                if (userId == null || userName == null) {
                    response.sendRedirect("login.jsp");
                } else {
                    // Pass session attributes to the request for account settings page
                    request.setAttribute("user_name", userName);
                    request.setAttribute("user_id", userId);
                    request.setAttribute("userRole", userRole);

                    // Forward the request to the account settings page
                    request.getRequestDispatcher("accountSettings").forward(request, response);
                }
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


            // Get the form data
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            // Validate user and get their role
            String userRole = getUserRole(username, password);
            int userId = getUserId(username, password);
            String userStatus = getUserStatus(username, password);

            if (userRole != null && userId != -1 && "active".equals(userStatus)) {
                // If valid, create a session and set the user's role
                HttpSession session = req.getSession(true); // Creates a new session if one doesn't exist
                session.setAttribute("user_id", userId);
                session.setAttribute("user_name", username);
                session.setAttribute("userRole", userRole);

                // Redirect to the dashboard
                resp.sendRedirect("index.jsp");
            } else {
                // If invalid, redirect to login page with error message
                req.setAttribute("error", "Invalid credentials or account is inactive!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    private String getUserRole(String username, String password) {
        String role = null;

        // Query to validate user and get their role
        String sql = "SELECT password, role FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set query parameters
            preparedStatement.setString(1, username);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    // Get the user's role
                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        role = resultSet.getString("role"); // Get role if password matches
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }

        return role;
    }
    private int getUserId(String username, String password) {
        int userId = -1; // Default value if user not found

        String sql = "SELECT password, user_id FROM users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");

                    // Verify if the provided password matches the stored hashed password
                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        userId = resultSet.getInt("user_id"); // Get user ID if password matches
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }
        return userId;
    }
    private String getUserStatus(String username, String password) {
        String status = null;

        // Query to check the user's status
        String sql = "SELECT status,password FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set query parameters
            preparedStatement.setString(1, username);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");

                    // Get the user's status
                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        status = resultSet.getString("status");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }

        return status;
    }
}