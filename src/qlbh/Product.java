package qlbh;

public class Product {
    private String id;
    private String name;
    private String image;
    private String category;
    private int price;
    private int quantity;
    private String status;
    private String description;

    public Product() {
    }

    public Product(int id, String name, double price, String image, int quantity, String status, String description) {
        this.id = String.valueOf(id);
        this.name = name;
        this.price = (int) price; // ép kiểu nếu cần đồng bộ với kiểu int của lớp
        this.image = image;
        this.quantity = quantity;
        this.status = status;
        this.description = description;
        this.category = ""; 
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

