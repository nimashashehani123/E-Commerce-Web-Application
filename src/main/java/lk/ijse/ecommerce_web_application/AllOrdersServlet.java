package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;

@WebServlet(name = "AllOrdersServlet",value = "/AllOrders")
public class AllOrdersServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query = "SELECT o.order_id, u.username, o.order_date, o.total_amount " +
                "FROM orders o JOIN users u ON o.user_id = u.user_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder ordersTable = new StringBuilder();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String username = rs.getString("username");
                Timestamp orderDate = rs.getTimestamp("order_date");
                double totalAmount = rs.getDouble("total_amount");

                ordersTable.append("<tr onclick=\"viewOrderDetails(").append(orderId).append(")\">");
                ordersTable.append("<td>").append(orderId).append("</td>");
                ordersTable.append("<td>").append(username).append("</td>");
                ordersTable.append("<td>").append(orderDate).append("</td>");
                ordersTable.append("<td>").append(totalAmount).append("</td>");
                ordersTable.append("</tr>");
            }
            request.setAttribute("orders", ordersTable.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view-all-orders.jsp");
        dispatcher.forward(request, response);
    }
}
