package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
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

@WebServlet("/productService")
public class ProductService extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public List<Product> getFilteredProducts(String search, String sortPrice, String minPriceParam, String maxPriceParam) {
        List<Product> products = new ArrayList<>();

        String query = "SELECT * FROM products WHERE 1=1";

        if (minPriceParam != null && !minPriceParam.isEmpty()) {
            query += " AND price >= ?";
        }
        if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
            query += " AND price <= ?";
        }
        if (search != null && !search.isEmpty()) {
            query += " AND name LIKE ?";
        }
        if (sortPrice != null) {
            if (sortPrice.equalsIgnoreCase("asc")) {
                query += " ORDER BY price ASC";
            } else if (sortPrice.equalsIgnoreCase("desc")) {
                query += " ORDER BY price DESC";
            }
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;

            // Set dynamic parameters
            if (minPriceParam != null && !minPriceParam.isEmpty()) {
                stmt.setDouble(paramIndex++, Double.parseDouble(minPriceParam));
            }
            if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
                stmt.setDouble(paramIndex++, Double.parseDouble(maxPriceParam));
            }
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
            }


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String imageFileName = rs.getString("image_url");
                String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getString("category_name"),
                        rs.getInt("qtyOnHand"),
                        imagePath
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

}
