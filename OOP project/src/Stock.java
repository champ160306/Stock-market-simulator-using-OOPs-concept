import java.io.*;
import java.util.*;

public class Stock {
    private String symbol;
    private List<DayData> history = new ArrayList<>();
    private int currentIndex = 0;

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    public Stock(String symbol, List<DayData> history) {
        this.symbol = symbol;
        this.history = history;
    }

    public void loadFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; } // skip header
                String[] parts = line.split(",");
                if (parts.length < 8) continue;
                String date = parts[7].trim();
                double open = Double.parseDouble(parts[1]);
                double high = Double.parseDouble(parts[2]);
                double low = Double.parseDouble(parts[3]);
                double close = Double.parseDouble(parts[4]);
                history.add(new DayData(date, open, high, low, close));
            }
        } catch (IOException e) {
            System.out.println("Error loading " + symbol + ": " + e.getMessage());
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        if (history.isEmpty()) return 0.0;
        return history.get(currentIndex).getClose();
    }

    public String getCurrentDate() {
        if (history.isEmpty()) return null;
        return history.get(currentIndex).getDate();
    }

    public double getCurrentOpenPrice() {
        if (history.isEmpty()) return 0.0;
        return history.get(currentIndex).getOpen();
    }

    public boolean moveNextDay() {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public void setCurrentDate(String date) {
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getDate().equals(date)) {
                currentIndex = i;
                return;
            }
        }
        currentIndex = 0; // fallback
    }

    @Override
    public String toString() {
        if (history.isEmpty()) return symbol + " → No data";
        DayData d = history.get(currentIndex);
        return symbol + " → " + d.toString();
    }
}
