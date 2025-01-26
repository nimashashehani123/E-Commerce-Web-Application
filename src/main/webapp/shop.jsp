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
    <div class="collapse navbar-collapse" id="navbarNav">
      <!-- Search & Filters Section -->
      <form class="d-flex ms-3" id="search-filter-form">
        <!-- Search Input -->
        <input class="form-control me-2" type="search" id="search-input" placeholder="Search products" aria-label="Search" onkeyup="filterProducts()">

        <!-- Sort by Price -->
        <select class="form-select" id="price-sort" onchange="sortByPrice()">
          <option value="">Sort by Price</option>
          <option value="low-to-high">Low to High</option>
          <option value="high-to-low">High to Low</option>
        </select>

        <!-- Price Range Filter -->
        <input type="number" class="form-control me-2" id="min-price" placeholder="Min Price" onchange="filterByPrice()">
        <input type="number" class="form-control me-2" id="max-price" placeholder="Max Price" onchange="filterByPrice()">
      </form>


      <!-- Navigation Items -->
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="index.jsp"><i class="fa-solid fa-house" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="categories"><i class="fa-solid fa-bag-shopping" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="cart"><i class="fa-solid fa-cart-shopping" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="user"><i class="fa-solid fa-user" style="color: #FFFFFF;"></i></a>
        </li>
      </ul>

    </div>
  </div>
</nav>


<%
  String message = request.getParameter("message");
  String error = request.getParameter("error");
%>
<div id="alert-container">
  <% if (message != null) { %>
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    <%= message %>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
  <% } %>
  <% if (error != null) { %>
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    <%= error %>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
  <% } %>
</div>

<div class="row" id="row" style="padding: 50px; overflow-x: auto; white-space: nowrap;scrollbar-width: none;">
  <!-- Categories Sidebar (Horizontally Scrollable) -->
  <div class="d-flex" style="display: flex; flex-wrap: nowrap; gap: 15px;">
    <!-- Dynamically Loaded Categories -->
    <%
      String userRole = (String) session.getAttribute("userRole");
      List<Category> categories = (List<Category>) request.getAttribute("categories");
    %>

    <!-- Add Category Option for Admin -->
    <% if ("admin".equals(userRole)) { %>
    <div class="category-item text-center" style="flex: 0 0 auto; min-width: 200px;">
      <a href="category_manage.jsp" class="btn btn-primary btn-block">Add Category</a>
    </div>
    <% } %>

    <!-- Categories List -->
    <% if (categories != null && !categories.isEmpty()) {
      %>
    <div class="category-item text-center" style="min-width: 140px;">
      <a href="categories?"><img src="https://i.pinimg.com/736x/70/06/67/70066731ebc0e43d97d37c2411c20d10.jpg" alt="All Products" class="rounded-circle" width="75" height="75"> </a>
    <div>All Products</div>
    </div>
    <%
      for (Category category : categories) {
    %>
    <div class="category-item text-center" style="flex: 0 0 auto; min-width: 140px;">
      <a href="categories?category_name=<%= category.getName() %>"><img src="<%= request.getContextPath() + category.getIconUrl() %>" alt="<%= category.getName() %> Image" class="rounded-circle" width="75" height="75">
      </a>
      <div><%= category.getName() %></div>
      <!-- Update and Delete Buttons for Admin -->
      <% if ("admin".equals(userRole)) { %>
      <div>
        <a href="categories?category_id=<%= category.getId() %>" class="btn btn-sm">
          <i class="fa-solid fa-pen-to-square" style="color: green;"></i>
        </a>
        <a href="#" class="btn btn-sm" onclick="confirmCategoryDelete('<%= category.getName() %>');">
          <i class="fa-solid fa-trash-can" style="color: red;"></i>
        </a>
      </div>
      <% } %>
    </div>
    <%
      }
    } else {
    %>
    <div class="category-item text-center" style="flex: 0 0 auto; min-width: 250px;">
      <p>No categories available</p>
    </div>
    <% } %>
  </div>
</div>




  <!-- Products Section -->
  <div class="col-lg-12 col-md-8 col-sm-12" style="padding: 30px" id="products-container">
    <%
      if ("admin".equals(userRole)) {
        String action = "getCategoryNames";
    %>
    <div class="mb-3">
      <a href="categories?action=<%=action%>" class="btn btn-primary">Add Product</a>
    </div>
    <% } %>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4">
      <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
          for (Product product : products) {
      %>
      <div class="col-lg-2 col-md-4 col-sm-6 mb-4">
        <div class="card">
          <img src="<%= request.getContextPath() + product.getImageUrl() %>"
               alt="<%= product.getName() %>"
               width="220vw" height="200vh">
          <div class="card-body">
            <h5 class="card-title"><%= product.getName() %></h5>
            <p class="card-text">Price: Rs.<%= product.getPrice() %></p>
            <p class="card-text">Qty Available: <%= product.getQtyOnHand() %></p>
            <%
              if (userRole != null) {
                if (userRole.equals("admin")) {
            %>
            <!-- Admin Controls -->
            <a href="addProduct?productId=<%= product.getId() %>" class="btn btn-sm" alt="update"><i class="fa-solid fa-pen-to-square" style="color: green;"></i></a>
            <a href="#"
               class="btn btn-sm" alt="delete"
               onclick="confirmProductDelete('<%= product.getId() %>');">
              <i class="fa-solid fa-trash-can" style="color: red;"></i>
            </a>
            <% } %>
            <% if (product.getQtyOnHand() > 0) { %>
          <% if (userRole.equals("customer")) { %>
           <%-- <a href="addToCart?productId=<%= product.getId() %>" class="btn btn-success">Add to Cart</a>--%>
          <form action="addToCart" method="post">
            <input type="hidden" name="productId" value="<%= product.getId() %>">
            <input type="hidden" name="quantity" value="1"> <!-- Default quantity -->
            <button type="submit" class="btn"><i class="fa-solid fa-cart-plus" style="color: #000000;"></i></button>
          </form>
            <% } %>
          <% } else { %>
            <button class="btn btn-secondary" disabled>Out of Stock</button>
            <% } %>
            <% } else { %>
            <a href="login.jsp" class="btn"><i class="fa-solid fa-cart-plus" style="color: #000000;"></i></a>
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
<script>
  function filterProducts() {
    // Get the values of the filters
    const search = document.getElementById('search-input').value;
    const sortBy = document.getElementById('price-sort').value;
    const minPrice = document.getElementById('min-price').value || 0;
    const maxPrice = document.getElementById('max-price').value || 100000;

    console.log(sortBy);

    // Create the AJAX request
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'categories?search=' + encodeURIComponent(search) + '&sortPrice=' + encodeURIComponent(sortBy) + '&minPrice=' + encodeURIComponent(minPrice) + '&maxPrice=' + encodeURIComponent(maxPrice), true);

    xhr.onload = function() {
      if (xhr.status === 200) {
        // Parse the response and update only the #products-container part
        const response = xhr.responseText;
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = response;

        // Get the updated products container from the response
        const updatedProducts = tempDiv.querySelector('#products-container');
        // Replace the content of the current products container with the updated one
        document.getElementById('products-container').innerHTML = updatedProducts.innerHTML;
      } else {
        console.error('Error:', xhr.status, xhr.statusText);
      }
    };

    // Send the request to the server
    xhr.send();
  }


  function sortByPrice() {
    // Trigger filtering when sorting by price
    filterProducts();
  }

  function filterByPrice() {
    // Trigger filtering when the price range filter is changed
    filterProducts();
  }

</script>
<script>
  function confirmProductDelete(productId) {
    if (confirm('Are you sure you want to delete this product?')) {
      // Create a form dynamically for POST request
      const form = document.createElement('form');
      form.method = 'POST';
      form.action = 'addProduct'; // Common servlet URL

      // Add hidden input for the product ID
      const productIdInput = document.createElement('input');
      productIdInput.type = 'hidden';
      productIdInput.name = 'productId';
      productIdInput.value = productId;

      // Add hidden input for the action type
      const actionInput = document.createElement('input');
      actionInput.type = 'hidden';
      actionInput.name = 'action';
      actionInput.value = 'delete';

      form.appendChild(productIdInput);
      form.appendChild(actionInput);
      document.body.appendChild(form);

      // Submit the form
      form.submit();
    }
  }
</script>
<script>
  function confirmCategoryDelete(categoryName) {
    if (confirm('Are you sure you want to delete this category?')) {
      // Create a form dynamically for POST request
      const form = document.createElement('form');
      form.method = 'POST';
      form.action = 'categories'; // Common servlet URL

      // Add hidden input for the product ID
      const productIdInput = document.createElement('input');
      productIdInput.type = 'hidden';
      productIdInput.name = 'categoryName';
      productIdInput.value = categoryName;

      // Add hidden input for the action type
      const actionInput = document.createElement('input');
      actionInput.type = 'hidden';
      actionInput.name = 'action';
      actionInput.value = 'delete';

      form.appendChild(productIdInput);
      form.appendChild(actionInput);
      document.body.appendChild(form);

      // Submit the form
      form.submit();
    }
  }
</script>
<script>
    setTimeout(function () {
    const alertContainer = document.getElementById('alert-container');
    if (alertContainer) {
    alertContainer.style.transition = "opacity 0.5s ease";
    alertContainer.style.opacity = "0";
    setTimeout(() => alertContainer.remove(), 500); // Remove it from DOM after fade-out
  }
  }, 3000);
</script>
</body>
</html>

