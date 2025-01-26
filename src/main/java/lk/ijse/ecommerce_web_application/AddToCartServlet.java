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

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try (Connection conn = dataSource.getConnection()){
            String checkQuery = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {

                    int newQuantity = rs.getInt("quantity") + quantity;
                    String updateQuery = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, newQuantity);
                        updateStmt.setInt(2, userId);
                        updateStmt.setInt(3, productId);
                        updateStmt.executeUpdate();
                    }
                } else {

                    String insertQuery = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, quantity);
                        insertStmt.executeUpdate();
                    }
                }

                String countQuery = "SELECT SUM(quantity) AS totalItems FROM cart WHERE user_id = ?";
                try (PreparedStatement countStmt = conn.prepareStatement(countQuery)) {
                    countStmt.setInt(1, userId);
                    ResultSet countRs = countStmt.executeQuery();

                    if (countRs.next()) {
                        int cartItemCount = countRs.getInt("totalItems");
                        HttpSession session = request.getSession();
                        session.setAttribute("cartItemCount", cartItemCount);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Redirect back to the shop page
        response.sendRedirect("categories");
    }
}
