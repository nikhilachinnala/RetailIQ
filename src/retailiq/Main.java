package retailiq;

import java.util.*;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final List<Product> PRODUCTS = new ArrayList<>();
    private static final List<String> ORDER_HISTORY = new ArrayList<>();
    private static int nextId = 109;

    public static void main(String[] args) {
        seedProducts();
        System.out.println("==============================================");
        System.out.println("       RetailIQ - Intelligent Retail Analytics");
        System.out.println("==============================================");
        while (true) {
            System.out.println("\n1. Admin Portal\n2. User Portal\n0. Exit");
            int choice = readInt("Choose portal: ");
            switch (choice) {
                case 1 -> adminPortal();
                case 2 -> userPortal();
                case 0 -> { System.out.println("Thank you for using RetailIQ!"); return; }
                default -> System.out.println("Please choose 0, 1, or 2.");
            }
        }
    }

    private static void adminPortal() {
        while (true) {
            System.out.println("\n--- ADMIN PORTAL ---");
            System.out.println("1. Dashboard summary");
            System.out.println("2. View products");
            System.out.println("3. Add product");
            System.out.println("4. Restock product");
            System.out.println("5. Sales and revenue summary");
           
            System.out.println("0. Back");
            switch (readInt("Choose option: ")) {
                case 1 -> dashboard();
                case 2 -> showProducts();
                case 3 -> addProduct();
                case 4 -> restockProduct();
                case 5 -> salesSummary();
                
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void userPortal() {
        while (true) {
            System.out.println("\n--- USER PORTAL ---");
            System.out.println("1. Browse products");
            System.out.println("2. Smart product search (KMP + typo suggestions)");
            System.out.println("3. Smart budget selection");
            System.out.println("4. Place demo order");
            System.out.println("5. View my order history");
            System.out.println("0. Back");
            switch (readInt("Choose option: ")) {
                case 1 -> showProducts();
                case 2 -> smartSearch();
                case 3 -> budgetSelection();
                case 4 -> placeOrder();
                case 5 -> showOrders();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void dsaLab() {
        while (true) {
            System.out.println("\n--- DSA ANALYTICS LAB ---");
            System.out.println("1. M1 String Algorithms (KMP, Z, Rabin-Karp)");
            System.out.println("2. M2 Suffix Structures (Suffix Array + LCP)");
            System.out.println("3. M3 Advanced DP (Edit Distance, Budget DP, Matrix Chain)");
            System.out.println("4. M4 Network Flow (Edmonds-Karp)");
            System.out.println("5. M5 NP-Completeness & Approximation (Clique, Vertex Cover)");
            System.out.println("6. M6 Randomized & Parallel Algorithms");
            System.out.println("0. Back");
            switch (readInt("Select module: ")) {
                case 1 -> module1();
                case 2 -> module2();
                case 3 -> module3();
                case 4 -> module4();
                case 5 -> module5();
                case 6 -> module6();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void module1() {
        String text = readLine("Text (default: wireless mouse and wireless keyboard): ");
        if (text.isBlank()) text = "wireless mouse and wireless keyboard";
        String pattern = readLine("Pattern to find (default: wireless): ");
        if (pattern.isBlank()) pattern = "wireless";
        System.out.println("KMP positions: " + Algorithms.kmp(text.toLowerCase(), pattern.toLowerCase()));
        System.out.println("Rabin-Karp positions: " + Algorithms.rabinKarp(text.toLowerCase(), pattern.toLowerCase()));
        System.out.println("Z array for pattern + $ + text: " +
                Arrays.toString(Algorithms.zArray(pattern + "$" + text)));
    }

    private static void module2() {
        String text = readLine("Enter text (default: banana): ");
        if (text.isBlank()) text = "banana";
        int[] sa = Algorithms.suffixArray(text);
        System.out.println("Suffix array: " + Arrays.toString(sa));
        for (int index : sa) System.out.println("  " + index + ": " + text.substring(index));
        System.out.println("LCP array: " + Arrays.toString(Algorithms.lcpArray(text, sa)));
    }

    private static void module3() {
        String a = readLine("First word (default: iphne): ");
        if (a.isBlank()) a = "iphne";
        String b = readLine("Second word (default: iphone): ");
        if (b.isBlank()) b = "iphone";
        System.out.println("Edit distance = " + Algorithms.editDistance(a.toLowerCase(), b.toLowerCase()));
        System.out.println("Matrix-chain demo dims [10, 30, 5, 60] cost = " +
                Algorithms.matrixChainCost(new int[]{10, 30, 5, 60}));
        System.out.println("Budget DP is also available from User Portal -> Smart budget selection.");
    }

    private static void module4() {
        int[][] cap = {
                {0, 10, 5, 0, 0, 0},
                {0, 0, 0, 9, 4, 0},
                {0, 0, 0, 0, 8, 0},
                {0, 0, 0, 0, 0, 10},
                {0, 0, 0, 0, 0, 10},
                {0, 0, 0, 0, 0, 0}
        };
        System.out.println("Example warehouse/store network maximum flow = " + Algorithms.maxFlow(cap, 0, 5));
        System.out.println("Capacity values represent maximum units that can move along each route.");
    }

    private static void module5() {
        boolean[][] g = {
                {false,true,true,false,false},
                {true,false,true,true,false},
                {true,true,false,true,false},
                {false,true,true,false,true},
                {false,false,false,true,false}
        };
        System.out.println("Maximum clique vertices (small demo graph): " + Algorithms.maximumClique(g));
        System.out.println("Approximate vertex cover: " + Algorithms.approximateVertexCover(g));
        System.out.println("Maximum clique uses brute force; keep the graph small.");
    }

    private static void module6() {
        int[] numbers = {19, 3, 12, 8, 5, 22, 1, 17};
        System.out.println("Before randomized quicksort: " + Arrays.toString(numbers));
        Algorithms.randomizedQuickSort(numbers);
        System.out.println("After randomized quicksort:  " + Arrays.toString(numbers));
        System.out.println("Reservoir sample of product names: " +
                Algorithms.reservoirSample(PRODUCTS.stream().map(Product::getName).toList(), 3));
        System.out.println("Parallel sum of sample sales: " +
                Algorithms.parallelSum(new int[]{120, 85, 210, 45, 160}));
    }

    private static void dashboard() {
        int stock = PRODUCTS.stream().mapToInt(Product::getStock).sum();
        double revenue = PRODUCTS.stream().mapToDouble(p -> p.getUnitsSold() * p.getPrice()).sum();
        System.out.println("Products: " + PRODUCTS.size());
        System.out.println("Units currently in stock: " + stock);
        System.out.println("Demo orders placed: " + ORDER_HISTORY.size());
        System.out.printf("Demo revenue: ₹%.2f%n", revenue);
    }

    private static void showProducts() {
        System.out.println("\n--- PRODUCT CATALOG ---");
        for (Product p : PRODUCTS) System.out.println(p);
    }

    private static void addProduct() {
        String name = readLine("Product name: ");
        if (name.isBlank()) { System.out.println("Name cannot be empty."); return; }
        String category = readLine("Category: ");
        double price = readDouble("Price: ");
        int stock = readInt("Initial stock: ");
        PRODUCTS.add(new Product(nextId++, name, category.isBlank() ? "General" : category, price, Math.max(0, stock)));
        System.out.println("Product added.");
    }

    private static void restockProduct() {
        showProducts();
        int id = readInt("Product ID to restock: ");
        Product p = findById(id);
        if (p == null) { System.out.println("Product not found."); return; }
        p.addStock(readInt("Units to add: "));
        System.out.println("Stock updated: " + p);
    }

    private static void salesSummary() {
        int sold = PRODUCTS.stream().mapToInt(Product::getUnitsSold).sum();
        double revenue = PRODUCTS.stream().mapToDouble(p -> p.getUnitsSold() * p.getPrice()).sum();
        Product top = PRODUCTS.stream().max(Comparator.comparingInt(Product::getUnitsSold)).orElse(null);
        System.out.println("Units sold in this session: " + sold);
        System.out.printf("Revenue in this session: ₹%.2f%n", revenue);
        System.out.println("Top product by units sold: " + (top == null ? "N/A" : top.getName() + " (" + top.getUnitsSold() + ")"));
        System.out.println("Note: this prototype does not save sales after exit.");
    }

    private static void smartSearch() {
        String query = readLine("Search product: ").toLowerCase().trim();
        if (query.isEmpty()) return;
        List<Product> matches = new ArrayList<>();
        for (Product p : PRODUCTS)
            if (!Algorithms.kmp(p.getName().toLowerCase(), query).isEmpty()) matches.add(p);
        if (!matches.isEmpty()) {
            System.out.println("Matches:");
            matches.forEach(System.out::println);
            return;
        }
        Product nearest = PRODUCTS.stream().min(Comparator.comparingInt(
                p -> Algorithms.editDistance(p.getName().toLowerCase(), query))).orElse(null);
        System.out.println("No exact substring match.");
        if (nearest != null) System.out.println("Did you mean: " + nearest.getName() + "?");
    }

    private static void budgetSelection() {
        double budget = readDouble("Your budget in rupees: ");
        List<Integer> selected = Algorithms.selectWithinBudget(PRODUCTS, budget);
        if (selected.isEmpty()) { System.out.println("No products fit this budget."); return; }
        double total = 0;
        System.out.println("Suggested product combination:");
        for (int i : selected) { Product p = PRODUCTS.get(i); System.out.println(p); total += p.getPrice(); }
        System.out.printf("Total: ₹%.2f (budget: ₹%.2f)%n", total, budget);
    }

    private static void placeOrder() {
        showProducts();
        int id = readInt("Enter product ID to buy one unit: ");
        Product p = findById(id);
        if (p == null) { System.out.println("Product not found."); return; }
        if (!p.sellOne()) { System.out.println("Sorry, this product is out of stock."); return; }
        String order = "Order for " + p.getName() + " | ₹" + String.format("%.2f", p.getPrice());
        ORDER_HISTORY.add(order);
        System.out.println("Order placed successfully (demo). " + order);
    }

    private static void showOrders() {
        if (ORDER_HISTORY.isEmpty()) { System.out.println("No orders in this session yet."); return; }
        ORDER_HISTORY.forEach(System.out::println);
    }

    private static Product findById(int id) {
        return PRODUCTS.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    private static void seedProducts() {
        PRODUCTS.add(new Product(101, "Wireless Mouse", "Accessories", 599, 25));
        PRODUCTS.add(new Product(102, "Mechanical Keyboard", "Accessories", 2499, 12));
        PRODUCTS.add(new Product(103, "USB-C Hub", "Accessories", 1799, 8));
        PRODUCTS.add(new Product(104, "Noise Cancelling Headphones", "Audio", 4999, 6));
        PRODUCTS.add(new Product(105, "Smart Watch", "Wearables", 3499, 15));
        PRODUCTS.add(new Product(106, "Portable SSD", "Storage", 4299, 9));
        PRODUCTS.add(new Product(107, "Webcam HD", "Accessories", 1599, 18));
        PRODUCTS.add(new Product(108, "Bluetooth Speaker", "Audio", 2199, 11));
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(SC.nextLine().trim()); }
            catch (Exception e) { System.out.println("Enter a valid whole number."); }
        }
    }
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double d = Double.parseDouble(SC.nextLine().trim());
                if (d >= 0) return d;
            } catch (Exception ignored) {}
            System.out.println("Enter a valid non-negative amount.");
        }
    }
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }
}
