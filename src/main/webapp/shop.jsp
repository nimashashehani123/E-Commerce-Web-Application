<%@ page import="lk.ijse.ecommerce_web_application.Dto.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.ijse.ecommerce_web_application.Dto.Product" %><%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 18/01/2025
  Time: 13:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="CSS/Shop.css" rel="stylesheet">
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
        integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="index.jsp">Vixora</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <form class="d-flex" id="searchForm">
      <input class="form-control me-2" type="text" id="searchInput" placeholder="Search products..." aria-label="Search">
      <button class="btn btn-outline-light" type="button" id="searchButton">Search</button>
    </form>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="index.jsp"><i class="fa-solid fa-house" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="categories"><i class="fa-solid fa-bag-shopping" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="cart.jsp"><i class="fa-solid fa-cart-shopping" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="login.jsp"><i class="fa-solid fa-user" style="color: #FFFFFF;"></i></a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<div class="row" style="padding: 0">
  <!-- Sidebar for Categories -->
  <div class="col-lg-3 col-md-4 col-sm-12">
    <h4>Categories</h4>
    <!-- Add Category Option for Admin -->
    <%
      String userRole = (String) session.getAttribute("userRole"); // Get the user role
      if ("admin".equals(userRole)) { %>
    <div class="mb-3">
      <a href="category_manage.jsp" class="btn btn-primary btn-block">Add Category</a>
    </div>
    <% } %>
    <ul class="list-group">
      <!-- Categories loaded dynamically from the database -->
      <%
        List<Category> categories = (List<Category>) request.getAttribute("categories");

        if (categories != null && !categories.isEmpty()) {
      %>
      <li class="list-group-item">
        <img src="https://i.pinimg.com/736x/70/06/67/70066731ebc0e43d97d37c2411c20d10.jpg" class="rounded-circle me-2" width="50" height="50">
        <a href="categories">All Products</a>
      </li>
      <%
        for (Category category : categories) {
      %>
      <li class="list-group-item d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center">
          <!-- Display category image -->
          <img src="data:image/jpeg;base64,<%= category.getIconUrl() %>" alt="<%= category.getName() %> Image" class="rounded-circle me-2" width="50" height="50">
          <a href="categories?category_name=<%= category.getName() %>">
            <%= category.getName() %>
          </a>
        </div>
        <!-- Display update and delete options if the user is an admin -->
        <% if ("admin".equals(userRole)) { %>
        <span>
      <a href="updateCategoryForm?category_id=<%= category.getId() %>" class="btn btn-sm btn-warning">Update</a>
      <a href="deleteCategory?category_id=<%= category.getId() %>" class="btn btn-sm btn-danger">Delete</a>
    </span>
        <% } %>
      </li>
      <%
        }
      } else {
      %>
      <li class="list-group-item">No categories available</li>
      <%
        }
      %>
    </ul>
  </div>


  <!-- Products Section -->
  <div class="col-lg-9 col-md-8 col-sm-12">
    <h4>Products</h4>

    <%
      // Display "Add Product" button if user is an admin
      if ("admin".equals(userRole)) {
    %>
    <div class="mb-3">
      <a href="addProduct.jsp" class="btn btn-primary">Add Product</a>
    </div>
    <% } %>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4">
      <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
          for (Product product : products) {
      %>
      <div class="col-lg-4 col-md-6 col-sm-12 mb-4">
        <div class="card" style="padding-right: 0">
          <img src="data:image/jpeg;base64,<%= product.getBase64Image() %>" alt="<%= product.getName() %>" width="200vw" height="200vh" style="margin-left:5vw">
          <div class="card-body">
            <h5 class="card-title"><%= product.getName() %></h5>
            <p class="card-text">Price: $<%= product.getPrice() %></p>
            <p class="card-text">Qty Available: <%= product.getQtyOnHand() %></p>
            <%
              if (userRole != null) {
                if (userRole.equals("admin")) {
            %>
            <!-- Admin Controls -->
            <a href="editProduct.jsp?productId=<%= product.getId() %>" class="btn btn-warning">Edit</a>
            <a href="deleteProductServlet?productId=<%= product.getId() %>" class="btn btn-danger">Delete</a>
            <% } %>
            <% if (product.getQtyOnHand() > 0) { %> <!-- Ensure the product is in stock -->
            <a href="addToCartServlet?productId=<%= product.getId() %>" class="btn btn-success">Add to Cart</a>
            <% } else { %>
            <button class="btn btn-secondary" disabled>Out of Stock</button>
            <% } %>
            <% } else { %>
            <a href="login.jsp" class="btn btn-primary">Add to Cart</a>
            <% } %>
          </div>
        </div>
      </div>
      <% } %>
    </div>
    <% } else { %>
    <p>No products available in this category.</p>
    <% } %>
  </div>

</div>

<!-- Footer -->
<footer class="bg-dark text-white pt-5 pb-3" style="font-family: Candara">
  <div class="container">
    <div class="row">
      <!-- About Section -->
      <div class="col-lg-4 col-md-6 mb-4">
        <h5>About Us</h5>
        <p>
          Welcome to our shop! We offer a wide range of high-quality products at amazing prices. Your satisfaction is our priority.
        </p>
      </div>

      <!-- Quick Links Section -->
      <div class="col-lg-4 col-md-6 mb-4" >
        <div style="margin-left: 7vw;margin-right: 5vw" id="quicklinks">
          <h5>Quick Links</h5>
          <ul class="list-unstyled">
            <li><a href="shop.jsp" class="text-white text-decoration-none">Shop</a></li>
            <li><a href="cart.jsp" class="text-white text-decoration-none">Cart</a></li>
            <li><a href="about.jsp" class="text-white text-decoration-none">About Us</a></li>
            <li><a href="contact.jsp" class="text-white text-decoration-none">Contact</a></li>
          </ul>
        </div>
      </div>

      <!-- Contact Info Section -->
      <div class="col-lg-4 col-md-6 mb-4">
        <h5>Contact Us</h5>
        <p><i class="bi bi-geo-alt-fill"></i> 123 Main Street, Colombo, Sri Lanka</p>
        <p><i class="bi bi-envelope-fill"></i> Vixora@gmail.com</p>
        <p><i class="bi bi-telephone-fill"></i> +94 216 76 97</p>
      </div>
    </div>

    <!-- Social Media Links -->
    <div class="row">
      <div class="col text-center">
        <h5>Follow Us</h5>
        <a href="https://facebook.com" target="_blank" class="text-white me-3"><i class="bi bi-facebook"></i></a>
        <a href="https://instagram.com" target="_blank" class="text-white me-3"><i class="bi bi-instagram"></i></a>
        <a href="https://twitter.com" target="_blank" class="text-white me-3"><i class="bi bi-twitter"></i></a>
        <a href="https://linkedin.com" target="_blank" class="text-white"><i class="bi bi-linkedin"></i></a>
      </div>
    </div>

    <div class="row mt-3">
      <div class="col text-center">
        <p class="mb-0">&copy; 2025 Vixora. All rights reserved.</p>
      </div>
    </div>
  </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js"></script>
<script src="js/jquery-3.7.1.min.js"></script>
<%--<script>
  document.getElementById("searchButton").addEventListener("click", function () {
    const keyword = document.getElementById("searchInput").value;

    // Example of sending additional filters (optional)
    const category = ""; // Add a category value dynamically if needed
    const minPrice = ""; // Add a minimum price value if applicable
    const maxPrice = ""; // Add a maximum price value if applicable

    fetch(`http://localhost:8080/E_Commerce_Web_Application_war_exploded/search?keyword=${encodeURIComponent(keyword)}&category=${encodeURIComponent(category)}&minPrice=${minPrice}&maxPrice=${maxPrice}`)
            .then(response => response.json())
            .then(data => {
              // Clear the current product display
              const productContainer = document.querySelector(".row.row-cols-1.row-cols-sm-2.row-cols-md-3.row-cols-lg-4");
              productContainer.innerHTML = ""; // Clear existing content

              if (data && data.length > 0) {
                data.forEach(product => {
                  // Clear the current product display
                  const productContainer = document.querySelector(".row.row-cols-1");
                  productContainer.innerHTML = "";

                  // Loop through the search results and display them
                  data.forEach(product => {
                    const productCard = `
                  <div class="col-lg-4 col-md-6 col-sm-12 mb-4">
                    <div class="card">
                      <img src="${product.imageUrl || 'https://i.pinimg.com/736x/af/3f/df/af3fdfe68325570588649fb1e21b8b91.jpg'}"
                           class="card-img-top"
                           alt="${product.name || 'No Name'}" width="100vw" height="100vh">
                      <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        <p class="card-text">Price: $${product.price}</p>
                        <p class="card-text">Qty Available: ${product.qtyOnHand}</p>
                        <a href="addToCartServlet?productId=${product.id}" class="btn btn-success">Add to Cart</a>
                      </div>
                    </div>
                  </div>`;
                    productContainer.insertAdjacentHTML("beforeend", productCard);
                  });

                  // Handle cases where no products match the search
                  if (data.length === 0) {
                    productContainer.innerHTML = "<p>No products found.</p>";
                  }
                })
                        .catch(error => console.error('Error fetching search results:', error));
              }});
            });

</script>--%>
</body>
</html>

