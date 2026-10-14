package retailiq;

public class Product {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private int stock;
    private int unitsSold;

    public Product(int id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.unitsSold = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getUnitsSold() { return unitsSold; }

    public boolean sellOne() {
        if (stock <= 0) return false;
        stock--;
        unitsSold++;
        return true;
    }

    public void addStock(int amount) {
        if (amount > 0) stock += amount;
    }

    @Override
    public String toString() {
        return String.format("#%d | %-29s | %-12s | ₹%.2f | stock: %d",
                id, name, category, price, stock);
    }
}
