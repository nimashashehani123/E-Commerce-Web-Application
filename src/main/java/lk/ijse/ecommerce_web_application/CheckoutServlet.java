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
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<CartItem> cartItems = new ArrayList<>();
        double totalAmount = 0.0;

        try {
            conn = dataSource.getConnection();

            String sql = "SELECT c.cart_id, p.product_name, c.quantity, p.price, " +
                    "(c.quantity * p.price) AS subtotal " +
                    "FROM cart c " +
                    "INNER JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartId(rs.getInt("cart_id"));
                cartItem.setProductName(rs.getString("product_name"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setPrice(rs.getDouble("price"));
                cartItem.setSubtotal(rs.getDouble("subtotal"));

                totalAmount += rs.getDouble("subtotal");
                cartItems.add(cartItem);
            }

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("totalAmount", totalAmount);

            RequestDispatcher dispatcher = request.getRequestDispatcher("checkout.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
