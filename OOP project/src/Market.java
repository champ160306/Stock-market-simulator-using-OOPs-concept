import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Market {
    private Map<String, Stock> stocks = new HashMap<>();

    public void loadStocksFromFolder(String folderPath) {
        try {
            Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(path -> {
                        String symbol = path.getFileName().toString().replace(".csv", "");
                        Stock stock = new Stock(symbol);
                        stock.loadFromCSV(path.toString());
                        stocks.put(symbol, stock);
                    });
            System.out.println("Loaded " + stocks.size() + " stocks from folder.");
        } catch (IOException e) {
            System.out.println("Error loading stocks: " + e.getMessage());
        }
    }

    public void setMarketDate(String date) {
        for (Stock s : stocks.values()) {
            s.setCurrentDate(date);
        }
    }

    public String getCurrentDate() {
        if (stocks.isEmpty()) return null;
        return stocks.values().iterator().next().getCurrentDate();
    }

    public void addStock(Stock s) {
        stocks.put(s.getSymbol(), s);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void displayMarket() {
        System.out.println("------MARKET------");
        System.out.println("Symbol | Price");
        System.out.println("---------------");
        for (Stock s : stocks.values()) {
            System.out.printf("%-6s | ₹%.2f%n", s.getSymbol(), s.getCurrentOpenPrice());
        }
        System.out.println("-------------------------------------");
    }

    public void nextDay() {
        for (Stock s : stocks.values()) s.moveNextDay();
        System.out.println("Market moved to next day.\n");
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }
}
