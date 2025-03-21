package io.github.bonigarcia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockPortfolioTest {

    private StockPortfolio stockPortfolio;
    private iStockmarketService stockMarket;

    @BeforeEach
    public void setUp() {
        stockMarket = mock(iStockmarketService.class);
        stockPortfolio = new StockPortfolio(stockMarket, new ArrayList<>());
    }

    @Test
    public void getTotalValue() {
        Stock sAndP500Stock = new Stock("S&P 500", 500, 100.00);
        Stock nvidiaStock = new Stock("NVIDIA", 250, 50.00);

        stockPortfolio.addStock(sAndP500Stock);
        stockPortfolio.addStock(nvidiaStock);

        when(stockMarket.lookUpPrice("S&P 500")).thenReturn(100.00);
        when(stockMarket.lookUpPrice("NVIDIA")).thenReturn(50.00);

        double result = stockPortfolio.getTotalValue();

        assertThat(result).isEqualTo(62500.0);

        verify(stockMarket, times(2)).lookUpPrice(anyString());
    }

    // mostValuableStocks tests here
    @Test
    public void whenNIsBiggerThanNumberOfStocksInPortfolio_thenThrowIllegalArgumentException() {
        Stock sAndP500Stock = new Stock("S&P 500", 500, 100.00);
        Stock nvidiaStock = new Stock("NVIDIA", 250, 50.00);

        stockPortfolio.addStock(sAndP500Stock);
        stockPortfolio.addStock(nvidiaStock);

        assertThatThrownBy(() -> stockPortfolio.mostValuableStocks(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("N is bigger than the number of stocks in the portfolio");
    }

    @Test
    public void whenNIsNegative_thenThrowIllegalArgumentException() {
        Stock sAndP500Stock = new Stock("S&P 500", 500, 100.00);
        Stock nvidiaStock = new Stock("NVIDIA", 250, 50.00);

        stockPortfolio.addStock(sAndP500Stock);
        stockPortfolio.addStock(nvidiaStock);

        assertThatThrownBy(() -> stockPortfolio.mostValuableStocks(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("N cannot be negative");
    }

    @Test
    public void whenNIsZero_thenReturnEmptyList() {
        Stock sAndP500Stock = new Stock("S&P 500", 500, 100.00);
        Stock nvidiaStock = new Stock("NVIDIA", 250, 50.00);

        stockPortfolio.addStock(sAndP500Stock);
        stockPortfolio.addStock(nvidiaStock);

        ArrayList<Stock> result = stockPortfolio.mostValuableStocks(0);

        assertThat(result).isEmpty();
    }

    @Test
    void whenNIsValid_thenReturnTopNMostValuableStocks() {
        Stock sAndP500Stock = new Stock("S&P 500", 500, 100.00);
        Stock nvidiaStock = new Stock("NVIDIA", 250, 50.00);
        Stock teslaStock = new Stock("TESLA", 100, 200.00);

        stockPortfolio.addStock(sAndP500Stock);
        stockPortfolio.addStock(nvidiaStock);
        stockPortfolio.addStock(teslaStock);

        ArrayList<Stock> result = stockPortfolio.mostValuableStocks(2);

        assertThat(result).containsExactlyInAnyOrder(teslaStock, sAndP500Stock);
    }

    @AfterEach
    public void tearDown() {
        stockPortfolio = null;
        stockMarket = null;
    }
}
