package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.ijse.ecommerce_web_application.Dto.CartItem;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer userId = (Integer) request.getSession().getAttribute("user_id");
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }



        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<CartItem> cartItems = new ArrayList<>();

        try {

            conn = dataSource.getConnection();


            String query = "SELECT c.cart_id, p.product_name, c.quantity, p.price, (c.quantity * p.price) AS subtotal " +
                    "FROM cart c " +
                    "JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";


            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();


            while (rs.next()) {
                int cartId = rs.getInt("cart_id");
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double subtotal = rs.getDouble("subtotal");

                CartItem item = new CartItem(cartId, productName, quantity, price, subtotal);
                cartItems.add(item);
            }

            request.setAttribute("cartItems", cartItems);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close database resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        int delta = Integer.parseInt(request.getParameter("delta"));
        String message = null;

        try (Connection conn = dataSource.getConnection()) {
            int currentQuantity = 0;
            int qtyOnHand = 0;

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT c.quantity, p.qtyOnHand " +
                            "FROM cart c " +
                            "JOIN products p ON c.product_id = p.product_id " +
                            "WHERE c.cart_id = ? AND c.user_id = ?")) {
                stmt.setInt(1, cartId);
                stmt.setInt(2, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        currentQuantity = rs.getInt("quantity");
                        qtyOnHand = rs.getInt("qtyOnHand");
                    }
                }
            }

            if (delta > 0 && currentQuantity + delta > qtyOnHand) {
                response.sendRedirect("cart?error=Not enough stock available for this product.");

            } else if (delta < 0 && currentQuantity + delta < 1) {
                response.sendRedirect("cart?error=Quantity cannot be less than 1.");
            } else {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE cart SET quantity = quantity + ? WHERE cart_id = ? AND user_id = ?")) {
                    stmt.setInt(1, delta);
                    stmt.setInt(2, cartId);
                    stmt.setInt(3, userId);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error: Unable to update cart due to a server issue.";

        }

        if (message != null) {
            response.setContentType("text/plain");
            response.sendRedirect("cart?error"+message);
        } else {
            response.sendRedirect("cart");
        }
    }
}
