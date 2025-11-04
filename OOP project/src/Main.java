import java.util.*;

public class Main {
    public static void main(String[] args) {
        Market market = new Market();
        market.loadStocksFromFolder("data"); // load data from CSVs
        Scanner sc = new Scanner(System.in);

        // Check for saved game
        GameState.GameData saved = GameState.load();

        Portfolio portfolio;
        String currentDate;

        if (saved != null) {
            portfolio = new Portfolio(saved.balance);
            portfolio.setHoldings(saved.holdings);
            portfolio.setAvgBuyPrices(saved.avgPrices);
            currentDate = saved.date;

            // Set market to the saved date
            market.loadStocksFromFolder("data");
            market.setMarketDate(saved.date); // Set all stocks to saved date

            System.out.println("Loaded saved game. Date: " + currentDate);
        } else {
            portfolio = new Portfolio(100000);
            market.loadStocksFromFolder("data");
            market.setMarketDate("1-10-2020");
            System.out.println("Market set to date: " + market.getCurrentDate());
            currentDate = "1-10-2020";
        }


        String s;
        int q;
        Stock st;
        boolean running = true;

        while (running) {
            System.out.println("------STOCK MARKET SIMULATOR------");
            System.out.println("Date: " + currentDate);
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Next Day");
            System.out.println("6. Save & Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    market.displayMarket();
                    break;
                case 2:
                    System.out.print("Enter symbol: ");
                    s = sc.next().toUpperCase();
                    System.out.print("Enter qty: ");
                    q = sc.nextInt();
                    st = market.getStock(s);
                    if (st != null) {
                        new BuyOrder(s, q, st.getCurrentOpenPrice()).execute(portfolio);
                    }
                    else {
                        System.out.println("Stock not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter symbol: ");
                    s = sc.next().toUpperCase();
                    System.out.print("Enter qty: ");
                    q = sc.nextInt();
                    st = market.getStock(s);
                    if (st != null) {
                        new SellOrder(s, q, st.getCurrentPrice()).execute(portfolio);
                    }
                    else {
                        System.out.println("Stock not found!");
                    }
                    break;
                case 4:
                    portfolio.showPortfolio(market);
                    break;
                case 5:
                    market.nextDay();
                    currentDate = market.getCurrentDate(); // Get date from market, not from first stock
                    System.out.println("Advanced to date: " + currentDate);
                    break;
                case 6:
                    GameState.save(portfolio, market, currentDate);
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
        sc.close();
    }
}
