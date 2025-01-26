<%--
  Created by IntelliJ IDEA.
  User: Nimasha Shehani
  Date: 22/01/2025
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vixora.lk</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="CSS/Category_manage.css" rel="stylesheet">
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

<form action="categories" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="action" value="update">
    <div class="form-group">
        <label for="categoryId">category Id:</label>
        <input type="text" id="categoryId" name="categoryId" value="${categoryId}" readonly>
    </div>
    <div class="form-group">
        <label for="categoryName">category Name:</label>
        <input type="text" id="categoryName" name="categoryName" class="form-control" value="${categoryName}" required>
    </div>
    <!-- Hidden field to hold the file name -->
    <input type="hidden" id="hiddenFileName" name="FileName" value="${icon_url}">
    <div class="form-group">
        <label for="categoryImage">category Image:</label>
        <input type="file" id="categoryImage" name="categoryImage" class="form-control" accept="image/*">
        <div class="form-group">
            <!-- Placeholder for image preview -->
            <label>Image Preview:</label>
            <div id="imagePreview" style="margin-top: 10px;">
                <img id="preview" src="<%= request.getContextPath() + request.getAttribute("icon_url") %>" alt="No Image Selected" style="max-width: 100%; height: auto; display: none;">
            </div>
        </div>
    </div>
    <button type="submit" class="btn btn-success">Update Category</button>
</form>

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
    // Get the input element and the preview image element
    const fileInput = document.getElementById('categoryImage');
    const previewImage = document.getElementById('preview');

    // Event listener to handle file selection
    fileInput.addEventListener('change', function() {
        const file = fileInput.files[0];

        if (file) {
            // Create a URL for the selected image file
            const reader = new FileReader();
            reader.onload = function(e) {
                // Set the preview image source to the selected image
                previewImage.src = e.target.result;
                previewImage.style.display = 'block';  // Show the preview image
            };
            reader.readAsDataURL(file);  // Read the file as a data URL
        } else {
            previewImage.src = "";  // If no file is selected, clear the preview
            previewImage.style.display = 'none';  // Hide the preview image
        }
    });
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
