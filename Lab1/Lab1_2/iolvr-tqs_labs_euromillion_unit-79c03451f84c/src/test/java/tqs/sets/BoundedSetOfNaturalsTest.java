/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.sets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import tqs.euromillions.Dip;
import tqs.sets.BoundedSetOfNaturals;

/**
 * @author ico0
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;
    private BoundedSetOfNaturals setD;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(1);
        setB = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        setC = BoundedSetOfNaturals.fromArray(new int[] { 50, 60 });
        setD = new BoundedSetOfNaturals(2);
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = setD = null;
    }

    @DisplayName("Test add element to set")
    @Test
    public void testAddElement() {

        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());
        // Try to add a second element to a set with maxSize = 1
        assertThrows(IllegalArgumentException.class, () -> setA.add(100));
        assertEquals(1, setA.size());

        setD.add(11);
        assertEquals(1, setD.size(), "add: elements count not as expected.");
        // Try to add a duplicate element
        assertThrows(IllegalArgumentException.class, () -> setD.add(11));
        assertEquals(1, setD.size(), "add: elements count not as expected.");
        // Try to add a non-natural element
        assertThrows(IllegalArgumentException.class, () -> setD.add(-11));
        assertEquals(1, setD.size(), "add: elements count not as expected.");
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
        assertThrows(IllegalArgumentException.class, () -> BoundedSetOfNaturals.fromArray(notNaturalElems));

        int[] duplicateElems = new int[] { 10, 20, 30, 40, 50, 60, 60 };

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> BoundedSetOfNaturals.fromArray(duplicateElems));

        // empty array
        int[] emptyArray = new int[] {};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> BoundedSetOfNaturals.fromArray(emptyArray));

    }

    @DisplayName("Test set comparison")
    @Test
    public void testEquals() {

        // null set
        BoundedSetOfNaturals other = null;
        assertFalse(setB.equals(other));

        // different class
        assertFalse(setB.equals(new Object()));

        // different size set
        other = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30 });
        assertFalse(setB.equals(other));

        // different values set
        other = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 49, 60 });
        assertFalse(setB.equals(other));

        // same values set
        other = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        assertTrue(setB.equals(other));

        // same Dip
        assertTrue(setB.equals(setB));
    }

    @DisplayName("Test set intersection")
    @Test
    public void testIntersection() {

        // setup
        BoundedSetOfNaturals subsetB = BoundedSetOfNaturals.fromArray(new int[] { 10, 40, 50, 60 });
        BoundedSetOfNaturals wrongSubset = BoundedSetOfNaturals.fromArray(new int[] { 10, 40, 50, 60, 70 });
        // verify
        assertTrue(setB.intersects(subsetB));
        assertFalse(setB.intersects(wrongSubset));
    }
}
