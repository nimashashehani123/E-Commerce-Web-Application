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

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        int userId = Integer.parseInt(request.getSession().getAttribute("user_id").toString());

        try (Connection conn = dataSource.getConnection()) {
            String deleteQuery = "DELETE FROM cart WHERE cart_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setInt(1, cartId);
                stmt.executeUpdate();
            }

            String countQuery = "SELECT SUM(quantity) AS totalItems FROM cart WHERE user_id = ?";
            try (PreparedStatement countStmt = conn.prepareStatement(countQuery)) {
                countStmt.setInt(1, userId);
                ResultSet rs = countStmt.executeQuery();

                HttpSession session = request.getSession();
                if (rs.next()) {
                    int cartItemCount = rs.getInt("totalItems");
                    session.setAttribute("cartItemCount", cartItemCount > 0 ? cartItemCount : 0);
                } else {
                    session.setAttribute("cartItemCount", 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("cart");
    }
}
