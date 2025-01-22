package lk.ijse.ecommerce_web_application;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.ijse.ecommerce_web_application.Dto.Category;
import lk.ijse.ecommerce_web_application.Dto.Product;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@MultipartConfig
@WebServlet(name = "CategoryServlet",value = "/categories")
public class CategoryServlet extends HttpServlet {
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set content type
        response.setContentType("text/html");
        // Get category_id from request parameters
        String categoryIdParam = request.getParameter("category_id");

        // Check if category_id is present
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            // Handle the case where a specific category is requested (for updating)
            try {
                int categoryId = Integer.parseInt(categoryIdParam);

                // Fetch the category by category_id
                try (Connection conn = dataSource.getConnection()) {
                    String sql = "SELECT * FROM categories WHERE category_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, categoryId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String base64Image = null;
                        // Set the category details as request attributes to pass to the update form
                        request.setAttribute("categoryId", categoryId);
                        request.setAttribute("categoryName", rs.getString("category_name"));
                        byte[] imageBytes = rs.getBytes("icon_url");
                        if (imageBytes != null) {
                            base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        }
                        request.setAttribute("icon_url", base64Image);

                        // Forward to the updateCategory.jsp page
                        request.getRequestDispatcher("updateCategory.jsp").forward(request, response);
                    } else {
                        // Category not found
                        response.sendRedirect("categories?error=Category not found");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("categories?error=Error fetching category: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                // Invalid category_id
                response.sendRedirect("categories?error=Invalid category ID");
            }
        } else {
        // Get session and user role
        HttpSession session = request.getSession(false);
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;


        // Fetch selected category from request or default to '0' (all categories)
        String selectedCategoryName = request.getParameter("category_name") != null ? request.getParameter("category_name") : null;

        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {

            String sql = "SELECT * FROM categories";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    byte[] imageBytes = rs.getBytes("icon_url"); // Assuming LONGBLOB for image

                    // Convert image to base64 string
                    String base64Image = null;
                    if (imageBytes != null) {
                        base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    }
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            base64Image
                    );
                    categories.add(category);
                }
            }

            // Fetch products based on category filter
            String productQuery = "SELECT * FROM products WHERE (? IS NULL OR category_name = ?)";
            PreparedStatement productStmt = connection.prepareStatement(productQuery);
            productStmt.setString(1,selectedCategoryName);
            productStmt.setString(2, selectedCategoryName);
            ResultSet productRs = productStmt.executeQuery();

            while (productRs.next()) {
                byte[] imageBytes = productRs.getBytes("image_url"); // Assuming LONGBLOB for image

                // Convert image to base64 string
                String base64Image = null;
                if (imageBytes != null) {
                    base64Image = Base64.getEncoder().encodeToString(imageBytes);
                }
                Product product = new Product(
                        productRs.getInt("product_id"),
                        productRs.getString("product_name"),
                        productRs.getDouble("price"),
                        base64Image,
                        productRs.getInt("qtyOnHand"),
                        productRs.getString("category_name")
                );
                products.add(product);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching categories.");
            return;
        }
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

        RequestDispatcher dispatcher = request.getRequestDispatcher("shop.jsp");
        dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            // Retrieve form data
            String categoryName = req.getParameter("categoryName");
            Part imagePart = req.getPart("categoryImage");

            try (Connection conn = dataSource.getConnection()) {
                String sql = "INSERT INTO categories (category_name,icon_url) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, categoryName);


                // Read image data as InputStream and set it in the query
                if (imagePart != null && imagePart.getSize() > 0) {
                    InputStream imageStream = imagePart.getInputStream();
                    pstmt.setBinaryStream(2, imageStream, (int) imagePart.getSize());
                } else {
                    pstmt.setNull(2, java.sql.Types.BLOB); // Handle case with no image
                }

                // Execute the query
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    resp.sendRedirect("categories?message=Category added successfully!");
                } else {
                    resp.sendRedirect("categories?error=Failed to add category.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().println("Error: " + e.getMessage());
            }
        }else if ("update".equals(action)) {
            // Retrieve form data
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            String categoryName = req.getParameter("categoryName");
            Part imagePart = req.getPart("categoryImage");

            try (Connection conn = dataSource.getConnection()) {
                // SQL update query
                String sql = "UPDATE categories SET category_name = ?,icon_url = ? WHERE category_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, categoryName);

                // Handle image update
                if (imagePart != null && imagePart.getSize() > 0) {
                    InputStream imageStream = imagePart.getInputStream();
                    pstmt.setBinaryStream(2, imageStream, (int) imagePart.getSize());
                } else {
                    pstmt.setNull(2, java.sql.Types.BLOB);
                }

                pstmt.setInt(3, categoryId);

                // Execute update
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    resp.sendRedirect("updateCategory.jsp?message=Category updated successfully");
                } else {
                    resp.sendRedirect("updateCategory.jsp?productId=" + categoryId + "&error=Failed to update Category");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("updateCategory.jsp?productId=" + categoryId + "&error=Error occurred");
            }
        }else if ("delete".equals(action)) {
            String categoryName = req.getParameter("categoryName");
            try (Connection conn = dataSource.getConnection()) {
                // Check if the category has associated products
                String checkProductsSql = "SELECT COUNT(*) FROM products WHERE category_name = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkProductsSql);
                checkStmt.setString(1, categoryName);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // If products exist for this category, don't delete it and show an alert message
                    resp.sendRedirect("categories?error=Category has associated products and cannot be deleted.");
                } else {
                    // If no products exist, proceed with deletion
                    String deleteSql = "DELETE FROM categories WHERE category_name = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    deleteStmt.setString(1, categoryName);
                    deleteStmt.executeUpdate();
                    resp.sendRedirect("categories?message=Category deleted successfully");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendRedirect("categories?error=Error deleting category: " + e.getMessage());
            }
        }
    }
}
