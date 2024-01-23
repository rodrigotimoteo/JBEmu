package io.github.cpu;

import io.github.memory.Word;

import java.util.HashMap;

/**
 * This abstraction holds the registers inside the Sharp CPU as well as the
 * ability to return the Flags from Register F
 *
 * @author rodrigotimoteo
 */

public class CPURegisters {

    /**
     * Stores an Array with the possible register names (8 registers)
     */
    private final String[] registerNames = {"A", "B", "C", "D", "E", "F", "H", "L"};

    /**
     * Stores all the registers with their associated name (registers are kept
     * as words for consistency between memory objects)
     */
    private final HashMap<String, Word> registers = new HashMap<>();

    /**
     * Stores the CPU flags corresponding to register F
     */
    private final CPUFlags flags = new CPUFlags();

    /**
     * Stores the PC (and assigns it to the default value at the end of the boot
     * rom which is 0x0100)
     */
    private char programCounter = 0x0100;

    /**
     * Stores the SP (and assigns it to the default value at the end of the boot
     * rom which is 0xFFFE)
     */
    private char stackPointer = 0xFFFE;

    /**
     * Creates all the objects to insert in the HashMap to store the registers
     * as well as initializes them to their default value at startup
     */
    public CPURegisters() {
        for(String name : registerNames)
            registers.put(name, new Word());

        initRegisters();
    }

    /**
     * Flags object getter (contains F register)
     *
     * @return cpu flags object
     */
    public CPUFlags getFlags() {
        return flags;
    }

    /**
     * Program Counter getter method
     *
     * @return program counter value
     */
    public char getProgramCounter() {
        return programCounter;
    }

    /**
     * Increments the program counter by the given value
     *
     * @param value to increase program counter
     */
    public void incrementProgramCounter(int value) {
        programCounter += (char) value;
    }

    /**
     * Sets the value of program counter to the given one
     *
     * @param value to assign program counter
     */
    public void setProgramCounter(int value) {
        programCounter = (char) value;
    }

    /**
     * Stack Pointer getter method
     *
     * @return stack pointer value
     */
    public char getStackPointer() {
        return stackPointer;
    }

    /**
     * Sets the value of the stack pointer to the given one
     *
     * @param value to assign stack pointer
     */
    public void setStackPointer(int value) {
        stackPointer = (char) value;
    }

    /**
     * Register getter based on given register's name
     *
     * @param register to retrieve
     * @return word representing given registers name
     */
    public Word getRegister(String register) {
        if(register.equals("F")) return flags.getRegister();
        return registers.get(register);
    }

    /**
     * Setter for all registers
     *
     * @param register to assign new value
     * @param value to assign
     */
    public void setRegister(String register, int value) {
        if(register.equals("F")) flags.setValue(value);
        registers.get(register).setValue(value);
    }

    /**
     * Returns the result of the aggregation of register A with register F (
     * creating a 16bit Word)
     *
     * @return register A with register F (16 bit Word)
     */
    public int getAF() {
        return (getRegister("A").getValue() << 8) + getRegister("F").getValue();
    }

    /**
     * Returns the result of the aggregation of register B with register C (
     * creating a 16bit Word)
     *
     * @return register B with register C (16 bit Word)
     */
    public int getBC() {
        return (getRegister("B").getValue() << 8) + getRegister("C").getValue();
    }

    /**
     * Returns the result of the aggregation of register D with register E (
     * creating a 16bit Word)
     *
     * @return register D with register E (16 bit Word)
     */
    public int getDE() {
        return (getRegister("D").getValue() << 8) + getRegister("E").getValue();
    }

    /**
     * Returns the result of the aggregation of register H with register L (
     * creating a 16bit Word)
     *
     * @return register H with register L (16 bit Word)
     */
    public int getHL() {
        return (getRegister("H").getValue() << 8) + getRegister("L").getValue();
    }

    public void setAF(int value) {
        setRegister("A", (value & 0xff00) >> 8);
        setRegister("F", value & 0x00f0);
    }

    public void setBC(int value) {
        setRegister("B", (value & 0xff00) >> 8);
        setRegister("C", value & 0x00ff);
    }

    public void setDE(int value) {
        setRegister("D", (value & 0xff00) >> 8);
        setRegister("E", value & 0x00ff);
    }

    public void setHL(int value) {
        setRegister("H", (value & 0xff00) >> 8);
        setRegister("L", value & 0x00ff);
    }

    /**
     * Initializes all the registers to their default values
     */
    private void initRegisters() {
        setAF(0x01B0);
        setBC(0x0013);
        setDE(0x00D8);
        setHL(0x014D);
    }
}
