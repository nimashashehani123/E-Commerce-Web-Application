package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lk.ijse.ecommerce_web_application.Dto.Product;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet("/addProduct")
@MultipartConfig
public class ProductServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get productId from request parameter
        int productId = Integer.parseInt(request.getParameter("productId"));

        // Variables to hold product data
        String productName = null;
        double price = 0.0;
        String categoryName = null;
        int qtyOnHand = 0;
        String base64Image = null;

        try (Connection conn = dataSource.getConnection()) {
            // Query to fetch product details
            String sql = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                productName = rs.getString("product_name");
                price = rs.getDouble("price");
                categoryName = rs.getString("category_name");
                qtyOnHand = rs.getInt("qtyOnHand");

                // Convert image blob to Base64
                byte[] imageBytes = rs.getBytes("image_url");
                if (imageBytes != null) {
                    base64Image = Base64.getEncoder().encodeToString(imageBytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("productId", productId);
        request.setAttribute("productName", productName);
        request.setAttribute("price", price);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("qtyOnHand", qtyOnHand);
        request.setAttribute("base64Image", base64Image);

        // Forward to editProduct.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateProduct.jsp");
        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            String productName = request.getParameter("productName");
        String price = request.getParameter("price");
        String categoryName = request.getParameter("categoryName");
        String qtyOnHand = request.getParameter("qtyOnHand");
        Part imagePart = request.getPart("productImage"); // File upload

        try (Connection conn = dataSource.getConnection()) {
            // SQL query for inserting the product
            String sql = "INSERT INTO products (product_name, price, image_url, category_name, qtyOnHand) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set parameters for the prepared statement
            pstmt.setString(1, productName);
            pstmt.setDouble(2, Double.parseDouble(price));
            pstmt.setString(4, categoryName);
            pstmt.setInt(5, Integer.parseInt(qtyOnHand));

            // Read image data as InputStream and set it in the query
            if (imagePart != null && imagePart.getSize() > 0) {
                InputStream imageStream = imagePart.getInputStream();
                pstmt.setBinaryStream(3, imageStream, (int) imagePart.getSize());
            } else {
                pstmt.setNull(3, java.sql.Types.BLOB); // Handle case with no image
            }

            // Execute the query
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("categories?message=Product added successfully!");
            } else {
                response.sendRedirect("categories?error=Failed to add product.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
        }else if ("update".equals(action)) {


            // Retrieve form data
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            double price = Double.parseDouble(request.getParameter("price"));
            String categoryName = request.getParameter("categoryName");
            int qtyOnHand = Integer.parseInt(request.getParameter("qtyOnHand"));
            Part imagePart = request.getPart("productImage");

            try (Connection conn = dataSource.getConnection()) {
                // SQL update query
                String sql = "UPDATE products SET product_name = ?, price = ?, category_name = ?, qtyOnHand = ?, image_url = ? WHERE product_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, productName);
                pstmt.setDouble(2, price);
                pstmt.setString(3, categoryName);
                pstmt.setInt(4, qtyOnHand);

                // Handle image update
                if (imagePart != null && imagePart.getSize() > 0) {
                    InputStream imageStream = imagePart.getInputStream();
                    pstmt.setBinaryStream(5, imageStream, (int) imagePart.getSize());
                } else {
                    pstmt.setNull(5, java.sql.Types.BLOB);
                }

                pstmt.setInt(6, productId);

                // Execute update
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("updateProduct.jsp?message=Product updated successfully");
                } else {
                    response.sendRedirect("updateProduct.jsp?productId=" + productId + "&error=Failed to update product");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("updateProduct.jsp?productId=" + productId + "&error=Error occurred");
            }
        }else if ("delete".equals(action)) {
            // Delete product
            int productId = Integer.parseInt(request.getParameter("productId"));
            try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            response.sendRedirect("categories?message=Product delete successfully");
        } catch (SQLException e) {
                response.sendRedirect("categories?error=Failed to delete product.");
            }
        }
    }
}

