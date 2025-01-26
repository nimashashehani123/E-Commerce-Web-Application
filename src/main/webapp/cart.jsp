<%@ page import="lk.ijse.ecommerce_web_application.Dto.CartItem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 22/01/2025
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Vixora.lk</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="CSS/Cart.css" rel="stylesheet">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
              integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    </head>
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


<!-- Cart Content -->
<div class="container my-5">
    <h1 class="mb-4">Your Cart</h1>
    <%
        List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
    %>
    <table>
        <thead>
        <tr>
            <th>Product</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Subtotal</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) { %>
        <tr>
            <td><%= item.getProductName() %></td>
            <td>
                <div class="input-group">
                    <button class="btn btn-outline-secondary btn-sm btn-decrease" data-cart-id="<%= item.getCartId() %>">-</button>
                    <input type="text" class="form-control text-center" style="border: none; background-color: white" value="<%= item.getQuantity() %>" readonly>
                    <button class="btn btn-outline-secondary btn-sm btn-increase" data-cart-id="<%= item.getCartId() %>">+</button>
                </div>
            </td>
            <td>Rs.<%= item.getPrice() %></td>
            <td>Rs.<%= item.getSubtotal() %></td>
            <td>
                <form action="RemoveFromCartServlet" method="post">
                    <input type="hidden" name="cartId" value="<%= item.getCartId() %>">
                    <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                </form>
            </td>
        </tr>
        <%  }
        } else { %>
        <tr>
            <td colspan="5" class="text-center">Your cart is empty!</td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <% if (cartItems != null && !cartItems.isEmpty()) { %>
    <div class="d-flex justify-content-between">
        <a href="categories" class="btn btn-secondary">Continue Shopping</a>
        <a href="CheckoutServlet" class="btn" style="background: linear-gradient(90deg, #ff416c, #ff4b2b); color: white;">Checkout</a>
    </div>
    <% } else { %>
    <div class="text-center">
        <a href="categories" class="btn btn-secondary">Continue Shopping</a>
    </div>
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        $(".btn-increase").click(function () {
            const cartId = $(this).data("cart-id");
            updateQuantity(cartId, 1);
        });

        $(".btn-decrease").click(function () {
            const cartId = $(this).data("cart-id");
            updateQuantity(cartId, -1);
        });

        function updateQuantity(cartId, delta) {
            $.ajax({
                url: "cart",
                method: "POST",
                data: { cartId: cartId, delta: delta },
                success: function () {
                    location.reload();
                },
                error: function () {
                    alert("Not enough stock available for this product.");
                }
            });
        }
    });
</script>
</body>
</html>
