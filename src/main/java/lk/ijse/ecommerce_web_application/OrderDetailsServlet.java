package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.ecommerce_web_application.Dto.OrderItem;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/OrderDetailsServlet")
public class OrderDetailsServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM order_items WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setSubtotal(rs.getBigDecimal("subtotal"));
                orderItems.add(item);
            }

        } catch (Exception e) {
            throw new ServletException("Error fetching order details", e);
        }

        req.setAttribute("orderItems", orderItems);
        req.setAttribute("orderId", orderId);

        req.getRequestDispatcher("orderDetails.jsp").forward(req, resp);
    }
}

