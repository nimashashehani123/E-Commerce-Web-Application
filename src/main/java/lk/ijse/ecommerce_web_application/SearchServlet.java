/*
package lk.ijse.ecommerce_web_application;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.ecommerce_web_application.Dto.Product;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");

        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM products WHERE (name LIKE ? )";
            if (category != null && !category.isEmpty()) {
                query += " AND category_name = ?";
            }
            if (minPrice != null && maxPrice != null) {
                query += " AND price BETWEEN ? AND ?";
            }

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            int paramIndex = 3;

            if (category != null && !category.isEmpty()) {
                stmt.setString(paramIndex++, category);
            }
            if (minPrice != null && maxPrice != null) {
                stmt.setDouble(paramIndex++, Double.parseDouble(minPrice));
                stmt.setDouble(paramIndex++, Double.parseDouble(maxPrice));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("qty_on_hand"),
                        rs.getString("image_url")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert products to JSON
        String jsonResponse = new Gson().toJson(products);
        response.getWriter().write(jsonResponse);
    }
}

*/
