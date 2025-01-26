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
import java.io.FileOutputStream;
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
        String action = request.getParameter("action");

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
                        String imagePath = null;
                        // Set the category details as request attributes to pass to the update form
                        request.setAttribute("categoryId", categoryId);
                        request.setAttribute("categoryName", rs.getString("category_name"));
                        String imageFileName = rs.getString("icon_url");
                        // Construct the relative image URL (assuming images are in /uploads/)
                        imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                        request.setAttribute("icon_url", imagePath);

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
        } else if(action != null && action.equals("getCategoryNames")){
            List<String> categoryNames = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT category_name FROM categories";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        categoryNames.add(rs.getString("category_name"));
                    }
                 }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching category names.");
                return;
            }
            request.setAttribute("categoryNames", categoryNames);

            RequestDispatcher dispatcher = request.getRequestDispatcher("addProduct.jsp");
            dispatcher.forward(request, response);
        } else {
            List<Category> categories = new ArrayList<>();
            List<Product> products = new ArrayList<>();

        // Get session and user role
        HttpSession session = request.getSession(false);
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;


        // Fetch selected category from request or default to '0' (all categories)
        String selectedCategoryName = request.getParameter("category_name") != null ? request.getParameter("category_name") : null;

        try (Connection connection = dataSource.getConnection()) {

            String sql = "SELECT * FROM categories";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String imageFileName = rs.getString("icon_url");
                    // Construct the relative image URL (assuming images are in /uploads/)
                    String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";


                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            imagePath
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
                // Get the image file name from the database (image_url stores file name)
                String imageFileName = productRs.getString("image_url");
                // Construct the relative image URL (assuming images are in /uploads/)
                String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                // Create Product object
                Product product = new Product(
                        productRs.getInt("product_id"),
                        productRs.getString("product_name"),
                        productRs.getDouble("price"),
                        imagePath, // Pass the constructed image URL
                        productRs.getInt("qtyOnHand"),
                        productRs.getString("category_name")
                );

                products.add(product);
            }

            String search = request.getParameter("search");
            String sortPrice = request.getParameter("sortPrice");
            String maxPriceParam = request.getParameter("maxPrice");
            String minPriceParam = request.getParameter("minPrice");

            if ((search != null && !search.trim().isEmpty()) ||
                    (sortPrice != null && !sortPrice.trim().isEmpty()) ||
                    (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) ||
                    (minPriceParam != null && !minPriceParam.trim().isEmpty())) {

                if (products != null) {
                    products.clear(); // Clear the existing data
                }

               /* double maxPrice = maxPriceParam != null ? Double.parseDouble(maxPriceParam) : Double.MAX_VALUE; // Default to a very high number if not provided
                double minPrice = minPriceParam != null ? Double.parseDouble(minPriceParam) : 0; // Default to 0 if not provided
*/
                // Call your service method to fetch the filtered products

                List<Product> filteredProducts = getFilteredProducts(search, sortPrice, minPriceParam, maxPriceParam);
                if (filteredProducts != null) {
                    products.addAll(filteredProducts);
                }
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

    public List<Product> getFilteredProducts(String search, String sortPrice, String minPriceParam, String maxPriceParam) {
        List<Product> products = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM products WHERE 1=1";

        // Append conditions dynamically
        if (minPriceParam != null && !minPriceParam.isEmpty()) {
            query += " AND price >= ?";
        }
        if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
            query += " AND price <= ?";
        }
        if (search != null && !search.isEmpty()) {
            query += " AND product_name LIKE ?";
        }
        if (sortPrice != null) {
            if (sortPrice.equalsIgnoreCase("low-to-high")) {
                query += " ORDER BY price ASC";
            } else if (sortPrice.equalsIgnoreCase("high-to-low")) {
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

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String imageFileName = rs.getString("image_url");
                String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        imagePath,
                        rs.getInt("qtyOnHand"),
                        rs.getString("category_name")

                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            // Retrieve form data
            String categoryName = req.getParameter("categoryName");
            Part imagePart = req.getPart("categoryImage");

            String imageFileName = imagePart.getSubmittedFileName();

            // Directory to save uploaded images
            String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

            try (Connection conn = dataSource.getConnection()) {
                // Save the uploaded image
                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = imagePart.getInputStream()) {
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    fos.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String sql = "INSERT INTO categories (category_name,icon_url) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, categoryName);
                pstmt.setString(2, imageFileName);

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
            String filePath = req.getParameter("FileName");
            String fileName = filePath.replace("/uploads/","");

            String imageFileName;

            if (imagePart != null && imagePart.getSize() > 0) {
                imageFileName = imagePart.getSubmittedFileName();

                // Directory to save uploaded images
                String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

                // Save the uploaded image
                try (FileOutputStream fos = new FileOutputStream(uploadPath); InputStream is = imagePart.getInputStream()) {
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
                // SQL update query
                String sql = "UPDATE categories SET category_name = ?,icon_url = ? WHERE category_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, categoryName);

                // Handle image update
                if (imageFileName != null) {
                    pstmt.setString(2, imageFileName); // Store the image filename
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR); // If no new image is uploaded, set it to null
                }

                pstmt.setInt(3, categoryId);

                // Execute update
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    resp.sendRedirect("categories?message=Category updated successfully");
                } else {
                    resp.sendRedirect("categories?error=Failed to update Category");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("categories?error=Error occurred");
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
