/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.sets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import tqs.sets.BoundedSetOfNaturals;

/**
 * @author ico0
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(1);
        setB = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        setC = BoundedSetOfNaturals.fromArray(new int[] { 50, 60 });
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @Disabled("TODO revise test logic")
    @Test
    public void testAddElement() {

        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());

        setB.add(11);
        assertTrue(setB.contains(11), "add: added element not found in set.");
        assertEquals(7, setB.size(), "add: elements count not as expected.");
    }

    @DisplayName("Test add from array with invalid elements (non-natural / duplicates / empty array)")
    @Test
    public void testAddFromBadArray() {
        int[] notNaturalElems = new int[] { 10, -20, -30 };

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(notNaturalElems));

        int[] duplicateElems = new int[] { 10, 20, 30, 40, 50, 60, 60 };

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(duplicateElems));

        // empty array
        int[] emptyArray = new int[] {};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(emptyArray));
    }

    @DisplayName("Test set creation from array with invalid elements (non-natural / duplicates / empty array)")
    @Test
    public void testFromArray() {
        int[] notNaturalElems = new int[] { 10, -20, -30 };

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.fromArray(notNaturalElems));

        int[] duplicateElems = new int[] { 10, 20, 30, 40, 50, 60, 60 };

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.fromArray(duplicateElems));

        // empty array
        int[] emptyArray = new int[] {};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.fromArray(emptyArray));

    }

}
