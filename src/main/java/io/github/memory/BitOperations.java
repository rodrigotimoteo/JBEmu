package io.github.memory;

/**
 * Defines the behaviour to be followed by the Classes that deal directly with
 * memory storage
 *
 * @author rodrigotimoteo
 */

public interface BitOperations {

    /**
     * Method responsible for changing the value of a specific bit to 1
     *
     * @param bit bit to change
     */
    void setBit(int bit);

    /**
     * Method responsible for changing the value of a specific bit to 0
     *
     * @param bit bit to change
     */
    void resetBit(int bit);

    /**
     * Method responsible for testing (true if 1) a specific bit
     *
     * @param bit bit to test
     */
    boolean testBit(int bit);

}
