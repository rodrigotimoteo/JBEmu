package io.github.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    Word word;

    @BeforeEach
    void init() {
        word = new Word();
    }

    /**
     * Test the default object creation
     */
    @Test
    void defaultMemoryIs0x00() {
        assertEquals(word.getValue(), 0);
    }


    /**
     * Test the assigned value to the constructor
     */
    @Test
    void assignedMemoryIs0xFA() {
        assertEquals(new Word(0xFA).getValue(), 0xFA);
    }

    /**
     * Test the setting of a specific bit starting with 0x00
     *
     * @param bit to set and evaluate
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void setBitOf0x00(int bit) {
        word.setBit(bit);

        assertEquals(word.getValue(), (0x01 << bit) & 0xFF);
    }

    /**
     * Test the resetting of a specific bit starting with 0xFF
     *
     * @param bit to reset and evaluate
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void resetBitOf0xFF(int bit) {
        word.setValue(0xFF);
        word.resetBit(bit);

        assertEquals(word.getValue(), 0xFF - (0x01 << bit));
    }

    /**
     * Test to see if correct boolean is returned from testing a bit starting
     * from 0x00 (all should return false)
     *
     * @param bit to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void testBitsIf0x00(int bit) {
        assertFalse(word.testBit(bit));
    }

    /**
     * Test to see if correct boolean is returned from testing a bit starting
     * from 0xFF (all should return true)
     *
     * @param bit to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void testBitsIf0xFF(int bit) {
        word.setValue(0xFF);

        assertTrue(word.testBit(bit));
    }

    /**
     * Test exception throwing for invalid bit argument in setBit method
     *
     * @param bit to test
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 8, 10, 100})
    void testExceptionsSetBit(int bit) {
        assertThrows(IllegalArgumentException.class, () -> { word.setBit(bit); });
    }

    /**
     * Test exception throwing for invalid bit argument in resetBit method
     *
     * @param bit to test
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 8, 10, 100})
    void testExceptionsResetBit(int bit) {
        assertThrows(IllegalArgumentException.class, () -> { word.resetBit(bit); });
    }

    /**
     * Test exception throwing for invalid bit argument in testBit method
     *
     * @param bit to test
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 8, 10, 100})
    void testExceptionsTestBit(int bit) {
        assertThrows(IllegalArgumentException.class, () -> { word.testBit(bit); });
    }
}