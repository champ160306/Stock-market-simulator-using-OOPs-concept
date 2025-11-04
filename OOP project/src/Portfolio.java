import java.util.*;

public class Portfolio implements Tradeable {
    private double balance;
    private Map<String, Integer> holdings = new HashMap<>();
    private Map<String, Double> avgBuyPrice = new HashMap<>();
    public Map<String, Integer> getHoldings() { return holdings; }

    public void setHoldings(Map<String, Integer> savedHoldings) {
        this.holdings = savedHoldings;
    }

    public Portfolio(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getAvgPrice(String symbol) {
        return avgBuyPrice.getOrDefault(symbol, 0.0);
    }

    public void setAvgBuyPrices(Map<String, Double> savedPrices) {
        this.avgBuyPrice = savedPrices;
    }

    @Override
    public void buy(String symbol, int qty, double price) {
        double cost = qty * price;
        if (cost <= balance) {
            balance -= cost;
            int existingQty = holdings.getOrDefault(symbol, 0);
            double existingAvg = avgBuyPrice.getOrDefault(symbol, 0.0);

            // Weighted average price if already holding
            double newAvgPrice = existingQty > 0 ?
                    ((existingAvg * existingQty) + (price * qty)) / (existingQty + qty)
                    : price;

            holdings.put(symbol, existingQty + qty);
            avgBuyPrice.put(symbol, newAvgPrice);
        } else {
            System.out.println("Not enough balance!");
        }
    }

    @Override
    public void sell(String symbol, int qty, double price) {
        int owned = holdings.getOrDefault(symbol, 0);
        if (owned >= qty) {
            balance += qty * price;
            holdings.put(symbol, owned - qty);
            if (holdings.get(symbol) == 0) {
                holdings.remove(symbol);
                avgBuyPrice.remove(symbol);
            }
            System.out.println("Sold " + qty + " shares of " + symbol + " at ₹" + price);
        } else {
            System.out.println("Not enough shares to sell! You own " + owned + " shares of " + symbol + ", but trying to sell " + qty);
        }
    }

    public void showPortfolio(Market m) {
        System.out.println("\n------ YOUR PORTFOLIO ------");
        double totalValue = 0;
        double totalGain = 0;

        if (holdings.isEmpty()) {
            System.out.println("You do not own any stocks.");
        }

        for (Map.Entry<String, Integer> e : holdings.entrySet()) {
            String symbol = e.getKey();
            int qty = e.getValue();
            Stock s = m.getStock(symbol);

            if (s != null) {
                double currentPrice = s.getCurrentPrice();
                double buyPrice = avgBuyPrice.getOrDefault(symbol, currentPrice);
                double value = qty * currentPrice;
                double gain = (currentPrice - buyPrice) * qty;
                double gainPercent = ((currentPrice - buyPrice) / buyPrice) * 100;

                totalValue += value;
                totalGain += gain;

                System.out.printf("%s: %d shares @ %.2f (Now: %.2f) | Gain: ₹%.2f (%.2f%%)%n",
                        symbol, qty, buyPrice, currentPrice, gain, gainPercent);
            } else {
                System.out.printf("%s: %d shares (Stock data not found)%n", symbol, qty);
            }
        }

        System.out.printf("Cash Balance: ₹%.2f%n", balance);
        System.out.printf("Total Portfolio Value: ₹%.2f%n", (balance + totalValue));
        System.out.printf("Overall Gain/Loss: ₹%.2f%n", totalGain);
        System.out.println("-----------------------------\n");
    }

    public double getBalance() { return balance; }
}
