<%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 19/01/2025
  Time: 00:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="CSS/customer_rejister.css" rel="stylesheet">
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
<!-- Main Content -->
<section>
    <div id="section">
    <div class="container" id="section1" >
        <!-- Register Section -->
        <div class="register-section">
            <h2>Register</h2>
            <form action="RegisterServlet" method="post" id="registerForm">
                <input type="hidden" name="action" value="save">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="password-wrapper">
                        <input type="password" id="password" name="password" required>
                        <button type="button" class="toggle-password" data-target="#password">👁️</button>
                    </div>
                </div>
                <div class="form-group">
                    <label for="repassword">Re-enter Password</label>
                    <div class="password-wrapper">
                        <input type="password" id="repassword" name="repassword" required>
                        <button type="button" class="toggle-password" data-target="#repassword">👁️</button>
                    </div>
                    <small id="passwordError" style="color: red; display: none;">Passwords do not match!</small>
                </div>
                <button type="submit" class="btn">Register</button>
            </form>
        </div>

        <!-- Login Redirect Section -->
        <div class="login-redirect-section">
            <h2>Already have an account?</h2>
            <p>Click below to login and start shopping.</p>
            <button id="login" onclick="window.location.href='login.jsp'">Login</button>
        </div>
    </div>
        <div id="section2" style="max-height: 75%">
            <h3>Why Register?</h3>
            <p>
                Exclusive Shopping Access:<br>
                Only registered members can shop from our site. Gain access to premium products and services unavailable to non-registered visitors.<br><br>

                Personalized Experience:<br>
                Enjoy a customized shopping experience with tailored recommendations, saved preferences, and faster checkouts.<br><br>

                Order History & Tracking:<br>
                Easily view and manage your past orders and track your current ones in real-time.<br><br>

                Special Offers & Deals:<br>
                Get access to member-only discounts and exclusive deals curated just for you.<br><br>

                Secure Checkout:<br>
                Your personal information is safely stored, ensuring a seamless and secure shopping experience every time.<br><br></p>

            <h3>Ready to Register?</h3><br>
                <p>Become a part of our family and enjoy these amazing benefits today!</p>
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        // Show/Hide password toggle
        $('.toggle-password').on('click', function () {
            const target = $(this).data('target'); // Get the target input field
            const input = $(target);
            const type = input.attr('type') === 'password' ? 'text' : 'password';
            input.attr('type', type); // Toggle between 'text' and 'password'

            // Optionally change the icon (if needed)
            $(this).text(type === 'password' ? '👁️' : '👁️‍🗨️');
        });

        // Validate password match on form submit
        $('#registerForm').on('submit', function (e) {
            const password = $('#password').val();
            const repassword = $('#repassword').val();

            if (password !== repassword) {
                e.preventDefault(); // Stop form submission
                $('#passwordError').show(); // Show error message
            } else {
                $('#passwordError').hide(); // Hide error message
            }
        });
    });
</script>
</body>
</html>

