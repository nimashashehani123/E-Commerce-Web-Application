<%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 25/01/2025
  Time: 08:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Settings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="CSS/Category_manage.css" rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <style>
      /*  body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f4f4f9;
        }

        .container {
            width: 80%;
            max-width: 600px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 20px;
            text-align: center;
        }

        h1 {
            margin-bottom: 20px;
        }

        .details {
            margin-bottom: 20px;
        }

        button {
            display: block;
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            font-size: 16px;
            color: #fff;
            background-color: #007bff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }

        .logout {
            background-color: #dc3545;
        }

        .logout:hover {
            background-color: #c82333;
        }*/
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="margin-bottom: 7vw">
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
<div class="container" style=" width: 80%;
            max-width: 600px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 20px;
            text-align: center;">
    <h1>Account Settings</h1>
    <div class="details" style="margin-bottom: 20px;">
        <p><strong>Username:</strong> <%= request.getAttribute("username") %></p>
        <p><strong>Email:</strong> <%= request.getAttribute("email") %></p>
    </div>
    <form action="user" method="get">
        <input type="hidden" name="userid" value="<%=request.getAttribute("userid") %>">
        <input type="hidden" name="action" value="updateAccount">
        <button type="submit">Change Account Details</button>
    </form>
    <form action="OrderListServlet">
        <button type="submit">View Orders</button>
    </form>
    <form action="LogoutServlet" method="post">
        <button type="submit" class="logout">Logout</button>
    </form>
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
</body>
</html>
