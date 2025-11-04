public abstract class Order {
    protected String symbol;
    protected int quantity;
    protected double price;

    public Order(String symbol, int quantity, double price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public abstract void execute(Portfolio p);
}

