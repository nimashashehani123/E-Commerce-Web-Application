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
import java.sql.*;
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user_id");

        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psOrderItems = null;
        PreparedStatement psClearCart = null;
        PreparedStatement psUpdateProductQty = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            String insertOrderSQL = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";
            psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            double totalAmount = calculateCartTotal(conn, userId);
            psOrder.setInt(1, userId);
            psOrder.setDouble(2, totalAmount);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            String cartQuery = " SELECT c.product_id, c.quantity, (c.quantity * p.price) AS subtotal, p.price FROM cart AS c INNER JOIN products AS p ON c.product_id = p.product_id WHERE c.user_id = ? ";
            PreparedStatement psCart = conn.prepareStatement(cartQuery);
            psCart.setInt(1, userId);
            ResultSet cartItems = psCart.executeQuery();

            String insertOrderItemsSQL = "INSERT INTO order_items (order_id, product_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
            psOrderItems = conn.prepareStatement(insertOrderItemsSQL);

            String updateProductQtySQL = "UPDATE products SET qtyOnHand = qtyOnHand - ? WHERE product_id = ?";
            psUpdateProductQty = conn.prepareStatement(updateProductQtySQL);

            while (cartItems.next()) {
                int productId = cartItems.getInt("product_id");
                int quantity = cartItems.getInt("quantity");

                psOrderItems.setInt(1, orderId);
                psOrderItems.setInt(2, productId);
                psOrderItems.setInt(3, quantity);
                psOrderItems.setDouble(4, cartItems.getDouble("price"));
                psOrderItems.setDouble(5, cartItems.getDouble("subtotal"));
                psOrderItems.addBatch();

                psUpdateProductQty.setInt(1, quantity);
                psUpdateProductQty.setInt(2, productId);
                psUpdateProductQty.addBatch();
            }

            psOrderItems.executeBatch();
            psUpdateProductQty.executeBatch();

            String deleteCartSQL = "DELETE FROM cart WHERE user_id = ?";
            psClearCart = conn.prepareStatement(deleteCartSQL);
            psClearCart.setInt(1, userId);
            psClearCart.executeUpdate();

            String countQuery = "SELECT SUM(quantity) AS totalItems FROM cart WHERE user_id = ?";
            try (PreparedStatement countStmt = conn.prepareStatement(countQuery)) {
                countStmt.setInt(1, userId);
                ResultSet rsCount = countStmt.executeQuery();

                if (rsCount.next()) {
                    int cartItemCount = rsCount.getInt("totalItems");
                    session.setAttribute("cartItemCount", cartItemCount > 0 ? cartItemCount : 0);
                } else {
                    session.setAttribute("cartItemCount", 0);
                }
            }

            conn.commit();
            response.sendRedirect("order_success.jsp");

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();

            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing your request. Please try again later.");
        } finally {
            try {
                if (psOrder != null) psOrder.close();
                if (psOrderItems != null) psOrderItems.close();
                if (psClearCart != null) psClearCart.close();
                if (psUpdateProductQty != null) psUpdateProductQty.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private double calculateCartTotal(Connection conn, int userId) throws SQLException {
        String sql = " SELECT SUM(c.quantity * p.price) AS total FROM cart AS c INNER JOIN products AS p ON c.product_id = p.product_id WHERE c.user_id = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0.0;
    }
}

