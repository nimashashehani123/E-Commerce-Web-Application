<%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 18/01/2025
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shopping Cart</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">Shop</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link" href="index.jsp">Home</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<div class="container mt-5">
  <h2>Your Cart</h2>

  <%--<%
    HttpSession session = request.getSession();
    List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");

    if (cart == null || cart.isEmpty()) {
  %>
  <p>Your cart is empty!</p>
  <% } else { %>
  <table class="table">
    <thead>
    <tr>
      <th>Product Name</th>
      <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <%
      double total = 0;
      for (Map<String, String> product : cart) {
        String productName = product.get("productName");
        double productPrice = Double.parseDouble(product.get("productPrice"));
        total += productPrice;
    %>
    <tr>
      <td><%= productName %></td>
      <td>$<%= productPrice %></td>
    </tr>
    <% } %>
    </tbody>
  </table>--%>

 <%-- <h3>Total: $<%= total %></h3>--%>
  <a href="index.jsp" class="btn btn-primary">Continue Shopping</a>
  <button class="btn btn-danger">Proceed to Checkout</button>
 <%-- <% } %>--%>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.min.js"></script>
</body>
</html>

