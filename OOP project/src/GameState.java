import java.io.*;
import java.util.*;

public class GameState {
    private static final String SAVE_FILE = "saves/gamestate.txt";

    public static void save(Portfolio portfolio, Market market, String currentDate) {
        try {
            File folder = new File("saves");
            if (!folder.exists()) folder.mkdirs();

            PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE));
            pw.println("DATE=" + currentDate);
            pw.println("BALANCE=" + portfolio.getBalance());

            // Save holdings and avg buy price
            for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                double avgPrice = portfolio.getAvgPrice(symbol);
                pw.println(symbol + "=" + qty + "," + avgPrice);
            }

            pw.close();
            System.out.println("Game saved successfully!\n");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }

    }

    public static GameData load() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No previous game found. Starting fresh.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String date = null;
            double balance = 0;
            Map<String, Integer> holdings = new HashMap<>();
            Map<String, Double> avgPrices = new HashMap<>();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("DATE=")) {
                    date = line.split("=")[1];
                } else if (line.startsWith("BALANCE=")) {
                    balance = Double.parseDouble(line.split("=")[1]);
                } else if (line.contains("=")) {
                    String[] parts = line.split("=");
                    String symbol = parts[0];
                    String[] valParts = parts[1].split(",");
                    int qty = Integer.parseInt(valParts[0]);
                    double avgPrice = valParts.length > 1 ? Double.parseDouble(valParts[1]) : 0.0;
                    holdings.put(symbol, qty);
                    avgPrices.put(symbol, avgPrice);
                }
            }

            return new GameData(date, balance, holdings, avgPrices);
        } catch (IOException e) {
            System.out.println("Error loading saved game: " + e.getMessage());
        }
        return null;
    }

    public static class GameData {
        public String date;
        public double balance;
        public Map<String, Integer> holdings;
        public Map<String, Double> avgPrices;

        public GameData(String date, double balance, Map<String, Integer> holdings, Map<String, Double> avgPrices) {
            this.date = date;
            this.balance = balance;
            this.holdings = holdings;
            this.avgPrices = avgPrices;
        }
    }
}
