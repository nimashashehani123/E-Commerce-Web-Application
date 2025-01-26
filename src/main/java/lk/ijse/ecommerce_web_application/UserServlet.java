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
        HttpSession session = request.getSession(false);


        if (session == null) {
            response.sendRedirect("login.jsp");
        } else {
            String action = request.getParameter("action");
            if ("updateAccount".equals(action)) {
                Integer userId = (session != null && session.getAttribute("user_id") != null)
                        ? (Integer) session.getAttribute("user_id")
                        : null;

                if (userId == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                String name = null;
                String email = null;

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

                request.setAttribute("name", name);
                request.setAttribute("email", email);

                request.getRequestDispatcher("updateAccount.jsp").forward(request, response);
            } else {

                String userRole = (String) session.getAttribute("userRole");
                Integer userId = (Integer) session.getAttribute("user_id");
                String userName = (String) session.getAttribute("user_name");

                if (userId == null || userName == null) {
                    response.sendRedirect("login.jsp");
                } else {
                    request.setAttribute("user_name", userName);
                    request.setAttribute("user_id", userId);
                    request.setAttribute("userRole", userRole);

                    request.getRequestDispatcher("accountSettings").forward(request, response);
                }
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


            String username = req.getParameter("username");
            String password = req.getParameter("password");

            String userRole = getUserRole(username, password);
            int userId = getUserId(username, password);
            String userStatus = getUserStatus(username, password);

            if (userRole != null && userId != -1 && "active".equals(userStatus)) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user_id", userId);
                session.setAttribute("user_name", username);
                session.setAttribute("userRole", userRole);

                resp.sendRedirect("index.jsp");
            } else {
                req.setAttribute("error", "Invalid credentials or account is inactive!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    private String getUserRole(String username, String password) {
        String role = null;

        String sql = "SELECT password, role FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        role = resultSet.getString("role");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }
    private int getUserId(String username, String password) {
        int userId = -1;

        String sql = "SELECT password, user_id FROM users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");

                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        userId = resultSet.getInt("user_id");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
    private String getUserStatus(String username, String password) {
        String status = null;

        String sql = "SELECT status,password FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");

                    if (PasswordVerifier.verifyPassword(password, storedPassword)) {
                        status = resultSet.getString("status");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}