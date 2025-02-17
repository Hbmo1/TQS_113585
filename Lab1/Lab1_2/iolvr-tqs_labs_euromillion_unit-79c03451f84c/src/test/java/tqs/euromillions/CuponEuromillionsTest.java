package tqs.euromillions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CuponEuromillionsTest {

    private CuponEuromillions cuponEuromillions;
    private Dip dip;

    @BeforeEach
    public void setUp() {
        cuponEuromillions = new CuponEuromillions();
        dip = new Dip(new int[] { 10, 20, 30, 40, 50 }, new int[] { 1, 2 });
    }

    @DisplayName("Test format against expected")
    @Test
    public void testFormat() {

        cuponEuromillions.appendDip(dip);
        String result = cuponEuromillions.format();
        assertEquals("Dip #1:N[ 10 20 30 40 50] S[  1  2]\n", result);
    }

    @AfterEach
    public void tearDown() {
        cuponEuromillions = null;
        dip = null;
    }
}
