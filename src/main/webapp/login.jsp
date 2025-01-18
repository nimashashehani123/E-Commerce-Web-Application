<%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 18/01/2025
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login and Registration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="CSS/Login.css" rel="stylesheet">
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#index.jsp">Vixora</a>
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
                    <a class="nav-link" href="shop.jsp"><i class="fa-solid fa-bag-shopping" style="color: #FFFFFF;"></i></a>
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

<section>
<div class="container" id="login">
    <!-- Registration Section -->
    <div class="registration-section">
        <h2>New Here?</h2>
        <img src="https://i.pinimg.com/736x/8f/61/91/8f61917133ba85e98fe93eea937aae75.jpg"width="200vw" height="200vh" style="margin-left: 10vw">
        <p>Click below to create your account and start shopping with us.</p>
        <button id="rejister" onclick="window.location.href='customer_rejistration.jsp'">Register</button>
    </div>
    
    <!-- Login Section -->
    <div class="login-section">
        <h2>Login</h2>
        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn">Login</button>
        </form>
    </div>

</div>

</section>


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

