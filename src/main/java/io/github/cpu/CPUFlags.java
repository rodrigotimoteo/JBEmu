package io.github.cpu;

import io.github.memory.Word;

/**
 * <p>This class is abstraction of the Flags used by the CPU to check for special
 * operations such as checking if output is Zero, checking the Carry and Half
 * Carry as well as the Subtract flag if a subtraction is performed</p>
 *
 * Bit 7 - Zero Flag
 * Bit 6 - Subtraction Flag (BCD)
 * Bit 5 - Half Carry Flag (BCD)
 * Bit 4 - Carry Flag
 *
 * @author rodrigotimoteo
 */

public class CPUFlags {

    /**
     * Stores the internal value of the Flags register
     */
    private final Word word = new Word();

    /**
     * Flag register getter
     *
     * @return flag register
     */
    public Word getRegister() {
        return word;
    }

    /**
     * Assigns the flag register a given value
     *
     * @param value to assign to the flag register
     */
    public void setValue(int value) {
        word.setValue(value);
    }

    /**
     * Sets the bit correspondent to the Zero Flag
     */
    public void setZeroFlag() {
        word.setBit(7);
    }

    /**
     * Sets the bit correspondent to the Subtract Flag
     */
    public void setSubtractFlag() {
        word.setBit(6);
    }

    /**
     * Sets the bit correspondent to the Half Carry Flag
     */
    public void setHalfCarryFlag() {
        word.setBit(5);
    }

    /**
     * Sets the bit correspondent to the Carry Flag
     */
    public void setCarryFlag() {
        word.setBit(4);
    }

    /**
     * Resets the bit correspondent to the Zero Flag
     */
    public void resetZeroFlag() {
        word.resetBit(7);
    }

    /**
     * Resets the bit correspondent to the Subtract Flag
     */
    public void resetSubtractFlag() {
        word.resetBit(6);
    }

    /**
     * Resets the bit correspondent to the Half Carry Flag
     */
    public void resetHalfCarryFlag() {
        word.resetBit(5);
    }

    /**
     * Resets the bit correspondent to the Carry Flag
     */
    public void resetCarryFlag() {
        word.resetBit(4);
    }

    /**
     * Zero flag boolean value getter
     *
     * @return test of Zero Flag bit (true if 1 false otherwise)
     */
    public boolean getZeroFlag() {
        return word.testBit(7);
    }

    /**
     * Subtract flag boolean value getter
     *
     * @return test of Subtract Flag bit (true if 1 false otherwise)
     */
    public boolean getSubtractFlag() {
        return word.testBit(6);
    }

    /**
     * Half Carry flag boolean value getter
     *
     * @return test of Half Carry Flag bit (true if 1 false otherwise)
     */
    public boolean getHalfCarryFlag() {
        return word.testBit(5);
    }

    /**
     * Carry flag boolean value getter
     *
     * @return test of Carry Flag bit (true if 1 false otherwise)
     */
    public boolean getCarryFlag() {
        return word.testBit(4);
    }

    /**
     * Converts the flags values into a readable string containing all the names
     * of flag followed by their value (boolean)
     *
     * @return String with full flags dump
     */
    @Override
    public String toString() {
        return "Flag Z: " + getZeroFlag() + " | N: " + getSubtractFlag() + " | H: " + getHalfCarryFlag() + " | C: " +
                getCarryFlag();
    }
}
