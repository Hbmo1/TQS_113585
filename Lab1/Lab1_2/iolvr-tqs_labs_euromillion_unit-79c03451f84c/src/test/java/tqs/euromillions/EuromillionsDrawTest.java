package tqs.euromillions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import tqs.euromillions.CuponEuromillions;
import tqs.euromillions.Dip;
import tqs.euromillions.EuromillionsDraw;

public class EuromillionsDrawTest {

    private CuponEuromillions sampleCoupon;

    @BeforeEach
    public void setUp() {
        sampleCoupon = new CuponEuromillions();
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(new Dip(new int[] { 1, 2, 3, 48, 49 }, new int[] { 1, 9 }));
    }

    @DisplayName("reports correct matches in a coupon")
    @Test
    public void testCompareBetWithDrawToGetResults() {
        Dip winningDip, matchesFound;

        // test for full match, using the 3rd dip in the coupon as the Draw results
        winningDip = sampleCoupon.getDipByIndex(2);
        EuromillionsDraw testDraw = new EuromillionsDraw(winningDip);
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);

        assertEquals(winningDip, matchesFound, "expected the bet and the matches found to be equal");

        // test for no matches at all
        testDraw = new EuromillionsDraw(new Dip(new int[] { 9, 10, 11, 12, 13 }, new int[] { 2, 3 }));
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);
        // compare empty with the matches found
        assertEquals(new Dip(), matchesFound);
    }

    @DisplayName("Generate a random draw")
    @Test
    public void testGenerateRandomDraw() {
        EuromillionsDraw draw = EuromillionsDraw.generateRandomDraw();
        assertNotNull(draw);
    }

    @DisplayName("Assure draw results are the same as the dip used to create the draw")
    @Test
    public void testDrawResults() {
        Dip dip = new Dip(new int[] { 1, 2, 3, 4, 5 }, new int[] { 1, 2 });
        EuromillionsDraw draw = new EuromillionsDraw(dip);
        assertEquals(dip, draw.getDrawResults());
    }

}
