package lk.ijse.ecommerce_web_application.Dto;

public class Product {
    int id;
    String name;
    Double price;
    String base64Image;
    int qtyOnHand;
    String categoryName;

    public Product() {
    }

    public Product(int id, String name, Double price, String base64Image, int qtyOnHand, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.base64Image = base64Image;
        this.qtyOnHand = qtyOnHand;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
