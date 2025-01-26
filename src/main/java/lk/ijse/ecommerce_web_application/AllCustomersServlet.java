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

@WebServlet(name = "AllCustomersServlet",value = "/AllCustomers")
public class AllCustomersServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query = "SELECT * FROM users WHERE role = 'customer'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder customersTable = new StringBuilder();
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String name = rs.getString("name");
                String status = rs.getString("status");

                customersTable.append("<tr>");
                customersTable.append("<td>").append(userId).append("</td>");
                customersTable.append("<td>").append(username).append("</td>");
                customersTable.append("<td>").append(email).append("</td>");
                customersTable.append("<td>").append(name).append("</td>");
                customersTable.append("<td>").append(status).append("</td>");
                customersTable.append("<td>");
                customersTable.append("<a href='ActivateCustomer?userId=").append(userId).append("'>Activate</a> | ");
                customersTable.append("<a href='DeactivateCustomer?userId=").append(userId).append("'>Deactivate</a>");
                customersTable.append("</td>");
                customersTable.append("</tr>");
            }
            request.setAttribute("customers", customersTable.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view-all-customers.jsp");
        dispatcher.forward(request, response);
    }
}
