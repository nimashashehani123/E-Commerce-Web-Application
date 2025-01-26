package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/accountSettings")
public class AccountSettingsServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");

        String username = (String) session.getAttribute("user_name");
        int userId = (int) session.getAttribute("user_id");
        String email = "";

        try (Connection conn = (dataSource.getConnection())) {

            String query = "SELECT email FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    email = rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("userid", userId);
        if(userRole.equals("customer")) {

            request.getRequestDispatcher("account.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("Adminaccount.jsp").forward(request, response);
        }
    }
}

