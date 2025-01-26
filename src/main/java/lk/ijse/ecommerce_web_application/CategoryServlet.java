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


        response.setContentType("text/html");

        String categoryIdParam = request.getParameter("category_id");
        String action = request.getParameter("action");


        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam);

                try (Connection conn = dataSource.getConnection()) {
                    String sql = "SELECT * FROM categories WHERE category_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, categoryId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String imagePath = null;

                        request.setAttribute("categoryId", categoryId);
                        request.setAttribute("categoryName", rs.getString("category_name"));
                        String imageFileName = rs.getString("icon_url");
                        imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                        request.setAttribute("icon_url", imagePath);

                        request.getRequestDispatcher("updateCategory.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("categories?error=Category not found");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("categories?error=Error fetching category: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
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

        HttpSession session = request.getSession(false);
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;


        String selectedCategoryName = request.getParameter("category_name") != null ? request.getParameter("category_name") : null;

        try (Connection connection = dataSource.getConnection()) {

            String sql = "SELECT * FROM categories";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String imageFileName = rs.getString("icon_url");
                    String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";


                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            imagePath
                    );
                    categories.add(category);
                }
            }

            String productQuery = "SELECT * FROM products WHERE (? IS NULL OR category_name = ?)";
            PreparedStatement productStmt = connection.prepareStatement(productQuery);
            productStmt.setString(1,selectedCategoryName);
            productStmt.setString(2, selectedCategoryName);
            ResultSet productRs = productStmt.executeQuery();

            while (productRs.next()) {
                String imageFileName = productRs.getString("image_url");
                String imagePath = (imageFileName != null && !imageFileName.isEmpty()) ? "/uploads/" + imageFileName : "/uploads/default.jpg";

                Product product = new Product(
                        productRs.getInt("product_id"),
                        productRs.getString("product_name"),
                        productRs.getDouble("price"),
                        imagePath,
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
                    products.clear();
                }

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

        String query = "SELECT * FROM products WHERE 1=1";

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
            String categoryName = req.getParameter("categoryName");
            Part imagePart = req.getPart("categoryImage");

            String imageFileName = imagePart.getSubmittedFileName();

            String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

            try (Connection conn = dataSource.getConnection()) {
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
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            String categoryName = req.getParameter("categoryName");
            Part imagePart = req.getPart("categoryImage");
            String filePath = req.getParameter("FileName");
            String fileName = filePath.replace("/uploads/","");

            String imageFileName;

            if (imagePart != null && imagePart.getSize() > 0) {
                imageFileName = imagePart.getSubmittedFileName();

                String uploadPath = getServletContext().getRealPath("/uploads/") + imageFileName;

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
                String sql = "UPDATE categories SET category_name = ?,icon_url = ? WHERE category_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, categoryName);

                if (imageFileName != null) {
                    pstmt.setString(2, imageFileName);
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }

                pstmt.setInt(3, categoryId);

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
                String checkProductsSql = "SELECT COUNT(*) FROM products WHERE category_name = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkProductsSql);
                checkStmt.setString(1, categoryName);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    resp.sendRedirect("categories?error=Category has associated products and cannot be deleted.");
                } else {
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
