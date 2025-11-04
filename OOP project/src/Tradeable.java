public interface Tradeable {
    void buy(String symbol, int qty, double price);
    void sell(String symbol, int qty, double price);
}
