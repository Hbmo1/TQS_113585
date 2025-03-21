package io.github.bonigarcia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class StockPortfolio implements iStockmarketService {
    private ArrayList<Stock> stocks = new ArrayList<>();
    private iStockmarketService stockMarket;

    public StockPortfolio(iStockmarketService stockMarket, ArrayList<Stock> stocks) {
        this.stockMarket = stockMarket;
        this.stocks = stocks;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public Stock getStock(int index) {
        return stocks.get(index);
    }

    public double getTotalValue() {
        return stocks.stream().mapToDouble(stock -> stock.getQuantity() * stockMarket.lookUpPrice(stock.getLabel()))
                .sum();
    }

    public ArrayList<Stock> mostValuableStocks(int topN) {
        if (topN > stocks.size()) {
            throw new IllegalArgumentException("N is bigger than the number of stocks in the portfolio");
        }
        if (topN < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }
        if (topN == 0) {
            return new ArrayList<Stock>();
        }
        return stocks.stream().sorted(Comparator.comparing(Stock::getPrice).reversed()).limit(topN)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public double lookUpPrice(String label) {
        if (stocks.stream().anyMatch(stock -> stock.getLabel().equals(label))) {
            return stocks.stream().filter(stock -> stock.getLabel().equals(label)).findFirst().get().getPrice();
        }
        return 0;
    }
}