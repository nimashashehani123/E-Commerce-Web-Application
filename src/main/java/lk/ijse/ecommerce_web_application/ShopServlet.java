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

@WebServlet(name = "ShopServlet",value = "/shop777")
public class ShopServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set content type
        response.setContentType("text/html");

        // Get session and user role
        HttpSession session = request.getSession(false);
        String userRole = (session != null && session.getAttribute("role") != null) ? session.getAttribute("role").toString() : null;

        // Fetch selected category from request or default to '0' (all categories)
        String selectedCategoryName = request.getParameter("category_name") != null ? request.getParameter("category_name") : null;

        // Establish DB connection
        try (Connection connection = dataSource.getConnection();) {
            // Fetch categories
            String categoryQuery = "SELECT * FROM categories";
            PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery);
            ResultSet categoryRs = categoryStmt.executeQuery();
            request.setAttribute("categories", categoryRs);

            // Fetch products based on category filter
            String productQuery = "SELECT * FROM products WHERE (? = 0 OR category_name = ?)";
            PreparedStatement productStmt = connection.prepareStatement(productQuery);
            productStmt.setString(1,selectedCategoryName);
            productStmt.setString(2, selectedCategoryName);
            ResultSet productRs = productStmt.executeQuery();
            request.setAttribute("products", productRs);

            // Forward to JSP
            request.getRequestDispatcher("shop.jsp").forward(request, response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
