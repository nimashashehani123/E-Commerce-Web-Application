<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Vixora.lk</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
  integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
  crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link href="CSS/Home.css" rel="stylesheet">
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
          <a class="nav-link active" aria-current="page" href="#"><i class="fa-solid fa-house" style="color: #FFFFFF;"></i></a>
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

<!-- Hero Section -->
<section>
<div class="hero">
  <div class="container text-center text-white">
    <!-- Title and Description -->
    <h1 class="display-4">Discover Fashion Like Never Before</h1>
    <p class="lead">Your destination for trendy, stylish, and affordable clothing.</p>
    <a href="categories" class="btn btn-lg mb-5">Shop Now</a>

    <!-- Scrollable Cards Section -->
    <div class="scroll-container">
      <!-- Card 1 -->
      <div class="card shadow border-0">
        <img src="https://i.pinimg.com/736x/af/3f/df/af3fdfe68325570588649fb1e21b8b91.jpg" class="card-img-top" alt="Trendy Outfits">
        <div class="card-body">
          <h5 class="card-title text-center">Trendy Outfits</h5>
          <h6 class="card-text text-center">Explore the latest styles <br>and make a statement with our unique<br> collection.</h6>
        </div>
      </div>

      <!-- Card 2 -->
      <div class="card shadow border-0">
        <img src="https://i.pinimg.com/736x/65/a1/08/65a10893a8b9c866631b000549167376.jpg" class="card-img-top" alt="Seasonal Trends">
        <div class="card-body">
          <h5 class="card-title text-center">Seasonal Trends</h5>
          <h6 class="card-text text-center">Stay ahead of the fashion <br>curve with clothing tailored for<br> every season.</h6>
        </div>
      </div>

      <!-- Card 3 -->
      <div class="card shadow border-0">
        <img src="https://i.pinimg.com/736x/b4/7c/0c/b47c0cb52761a558fd53a38169d389e7.jpg" class="card-img-top" alt="Exclusive Designs">
        <div class="card-body">
          <h5 class="card-title text-center">Exclusive Designs</h5>
          <h6 class="card-text text-center">Shop exclusive designs that you<br> won't find anywhere else.</h6>
        </div>
      </div>

      <!-- Card 4 -->
      <div class="card shadow border-0">
        <img src="https://i.pinimg.com/736x/77/5f/7c/775f7cfa959175fdfe31b543dd29f1d7.jpg" class="card-img-top" alt="Exclusive Designs">
        <div class="card-body">
          <h5 class="card-title text-center">Sustainable Fashion</h5>
          <h6 class="card-text text-center">Shop eco-friendly and <br> sustainable clothing that supports<br>  a better future.</h6>
        </div>
      </div>

      <!-- Card 5 -->
      <div class="card shadow border-0">
        <img src="https://i.pinimg.com/736x/7b/3a/f8/7b3af85139bf50c429934f96f11eae94.jpg" class="card-img-top" alt="Exclusive Designs">
        <div class="card-body">
          <h5 class="card-title text-center">Limited Edition</h5>
          <h6 class="card-text text-center">SDiscover our exclusive <br>limited edition pieces before <br>they're gone!</h6>
        </div>
      </div>
    </div>
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
