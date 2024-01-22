package io.github.memory;

/**
 * Defines the behaviour that should be followed by all objects that interact
 * with memory
 *
 * @author rodrigotimoteo
 */

public interface MemoryManipulation {

    /**
     * Method responsible for assigning a value for a specific address in the
     * memory managed by a specific memory module
     *
     * @param address where memory should be changed
     * @param value to assign
     */
    void setValue(int address, int value);

    /**
     * Method responsible for returning a value for a specific address in the
     * memory managed by a specific memory module
     *
     * @param address where memory should be retrieved
     * @return value in given address
     */
    int getValue(int address);

    /**
     * Method responsible for returning a Word for a specific address in the
     * memory managed by a specific memory module
     *
     * @param address where memory should be retrieved
     * @return Word (as object) in given address
     */
    Word getWord(int address);

}
