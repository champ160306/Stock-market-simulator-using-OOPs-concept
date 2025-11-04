public class BuyOrder extends Order {
    public BuyOrder(String symbol, int qty, double price) {
        super(symbol, qty, price);
    }

    @Override
    public void execute(Portfolio p) {
        p.buy(symbol, quantity, price);
        System.out.println("Bought " + quantity + " shares of " + symbol + " at " + price);
    }
}

