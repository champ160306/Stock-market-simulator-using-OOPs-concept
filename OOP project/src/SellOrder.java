public class SellOrder extends Order {
    public SellOrder(String symbol, int qty, double price) {
        super(symbol, qty, price);
    }

    @Override
    public void execute(Portfolio p) {
        p.sell(symbol, quantity, price);
    }
}

