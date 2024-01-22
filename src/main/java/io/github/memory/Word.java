package io.github.memory;

/**
 * <p>Defines the base memory unit in which the architecture of the GB is based</p>
 * <p>1 Word is equivalent to 1 byte or 8 bits</p>
 *
 * @author rodrigotimoteo
 */

public class Word implements BitOperations {

    /**
     * Holds the value of a word
     */
    private byte value;

    /** Constructor Method
     *
     * <p>Creates a new Word object with value set to 0x00</p>
     */
    public Word() {
        setValue(0x00);
    }

    /** Constructor Method
     *
     * <p>Creates a new Word object set to specified value</p>
     *
     * @param value to be assigned to Word
     */
    public Word(int value) {
        setValue(value);
    }

    /**
     * Method responsible for assigning a new value to a Word
     *
     * @param value to assign as integer
     */
    public void setValue(int value) {
        this.value = (byte) value;
    }

    /**
     * Method responsible for returning the current value held by Word
     *
     * @return value as integer
     */
    public int getValue() {
        return value & 0xff;
    }

    /**
     * Method responsible for changing the value of a specific bit to 1
     *
     * @param bit bit to change
     */
    @Override
    public void setBit(int bit) {
        if(bit < 0 || bit > 7)
            throw new IllegalArgumentException("Invalid bit");

        value = (byte) (value | (1 << bit));

    }

    /**
     * Method responsible for changing the value of a specific bit to 0
     *
     * @param bit bit to change
     */
    @Override
    public void resetBit(int bit) {
        if(bit < 0 || bit > 7)
            throw new IllegalArgumentException("Invalid bit");

        value = (byte) (value & ~(1 << bit));
    }

    /**
     * Method responsible for testing (true if 1) a specific bit
     *
     * @param bit bit to test
     */
    @Override
    public boolean testBit(int bit) {
        return false;
    }
}
