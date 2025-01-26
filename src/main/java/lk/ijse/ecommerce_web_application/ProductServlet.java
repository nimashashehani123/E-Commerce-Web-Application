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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
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
        int productId = Integer.parseInt(request.getParameter("productId"));

        String productName = null;
        double price = 0.0;
        String categoryName = null;
        int qtyOnHand = 0;
        String imagePath = null;

        try (Connection conn = dataSource.getConnection()) {

            String sql = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                productName = rs.getString("product_name");
                price = rs.getDouble("price");
                categoryName = rs.getString("category_name");
                qtyOnHand = rs.getInt("qtyOnHand");

                String imageFileName = rs.getString("image_url");
                imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("productId", productId);
        request.setAttribute("productName", productName);
        request.setAttribute("price", price);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("qtyOnHand", qtyOnHand);
        request.setAttribute("imagePath", imagePath);

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
            Part file = request.getPart("productImage"); // File upload

            String imageFileName = file.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

            try (Connection conn = dataSource.getConnection()) {
                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = file.getInputStream()) {
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    fos.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String sql = "INSERT INTO products (product_name, price, image_url, category_name, qtyOnHand) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, productName);
                pstmt.setDouble(2, Double.parseDouble(price));
                pstmt.setString(3, imageFileName);
                pstmt.setString(4, categoryName);
                pstmt.setInt(5, Integer.parseInt(qtyOnHand));


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
        }
        else if ("update".equals(action)) {

            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            double price = Double.parseDouble(request.getParameter("price"));
            String categoryName = request.getParameter("categoryName");
            int qtyOnHand = Integer.parseInt(request.getParameter("qtyOnHand"));
            Part file = request.getPart("productImage"); // File upload
            String filePath = request.getParameter("FileName");
            String fileName = filePath.replace("/uploads/","");

            String imageFileName ;

            if (file != null && file.getSize() > 0) {
                imageFileName = file.getSubmittedFileName();

                String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = file.getInputStream()) {
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    fos.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                imageFileName = fileName;
            }

            try (Connection conn = dataSource.getConnection()) {
                String sql = "UPDATE products SET product_name = ?, price = ?, category_name = ?, qtyOnHand = ?, image_url = ? WHERE product_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, productName);
                pstmt.setDouble(2, price);
                pstmt.setString(3, categoryName);
                pstmt.setInt(4, qtyOnHand);

                if (imageFileName != null) {
                    pstmt.setString(5, imageFileName);
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }

                pstmt.setInt(6, productId);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("categories?message=Product updated successfully");
                } else {
                    response.sendRedirect("categories?error=Failed to update product");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("categories?error=Error occurred");
            }
        }
      else if ("delete".equals(action)) {
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

