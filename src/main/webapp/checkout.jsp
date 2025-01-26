<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.ijse.ecommerce_web_application.Dto.CartItem" %><%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 24/01/2025
  Time: 12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="CSS/Checkout.css" rel="stylesheet">
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
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="index.jsp"><i class="fa-solid fa-house" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="categories"><i class="fa-solid fa-bag-shopping" style="color: #FFFFFF;"></i></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="cart">
            <i class="fa-solid fa-cart-shopping" style="color: #FFFFFF;"></i>
            <span class="badge bg-danger" id="cart-count">
        <%= session.getAttribute("cartItemCount") != null ? session.getAttribute("cartItemCount") : 0 %>
      </span>
          </a>
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

<%
  List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
  double totalAmount = (double) request.getAttribute("totalAmount");
%>

<h1>Checkout</h1>
<table border="1">
  <thead>
  <tr>
    <th>Product</th>
    <th>Quantity</th>
    <th>Price</th>
    <th>Subtotal</th>
  </tr>
  </thead>
  <tbody>
  <% if (cartItems != null && !cartItems.isEmpty()) {
    for (CartItem item : cartItems) { %>
  <tr>
    <td><%= item.getProductName() %></td>
    <td><%= item.getQuantity() %></td>
    <td>Rs.<%= item.getPrice() %></td>
    <td>Rs.<%= item.getSubtotal() %></td>
    <td>
    </td>
  </tr>
  <%  }
  } else { %>
  <tr>
    <td colspan="5">Your cart is empty!</td>
  </tr>
  <% } %>
  </tbody>
</table>

<% if (cartItems != null && !cartItems.isEmpty()) { %>
<h3>Total: Rs.<%= totalAmount %></h3>
<form action="OrderServlet" method="post" style="display: flex; justify-content: center; margin-top: 20px;">
  <button type="submit" style="background: linear-gradient(90deg, #ff416c, #ff4b2b); color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; transition: background-color 0.3s ease;" >Place Order</button>
</form>
<% } else { %>
<p>Please add items to your cart.</p>
<% } %>

<a href="categories" style=" display: block;
    text-align: center;
    margin: 20px 0;
    text-decoration: none;
    color: #4CAF50;
    font-weight: bold;
    font-size: 16px;">Continue Shopping</a>

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
</body>
</html>
