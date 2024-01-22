package io.github.memory;

import java.util.Arrays;

/**
 * Represents a single memory module, the full memory of the system is a set of
 * modules where each module represents a specific address range (ROM, ERAM,
 * WRAM, etc.) This specific object is responsible for managing the different
 * banks as well as resolving the specific address of where to write or read
 *
 * @author rodrigotimoteo
 */

public class MemoryModule implements MemoryManipulation {

    /**
     * Stores the number of banks that exist in this memory module
     */
    private final int numberOfBanks;

    /**
     * Stores a Matrix that represents the address space allocated to specific
     * module as well as number of banks that can be used by default the value
     * will be 1
     */
    private final Word[][] memory;

    /**
     * Store the offset of the memory address being access in this specific
     * memory module (for example WRAM starts at 0xC000 of address space but
     * in the array it starts from 0)
     */
    private final int offset;

    /**
     * Stores the number of simultaneous banks that can be accessed
     */
    private final int simultaneousBanks;

    /**
     * If there are more than 1 simultaneousBanks that can be accessed we need
     * to know which bank is currently active
     */
    private int activeBank;

    /** Constructor Method
     *
     * <p>Creates a new Memory Module using by default only one bank</p>
     *
     * @param size of the memory module to create
     * @param offset to find desired address (due to no alignment between address
     *               space and arrays)
     */
    public MemoryModule(int size, int offset) {
        numberOfBanks = 1;
        activeBank = 0;

        memory = new Word[numberOfBanks][size];

        initMemory();

        this.offset = offset;
        this.simultaneousBanks = 1;
    }

    /** Constructor Method
     *
     * <p>Creates a new Memory Module being able to use more than one
     * simultaneous memory bank</p>
     *
     * @param size of the memory module to create
     * @param simultaneousBanks accessible at a time
     * @param offset to find desired address (due to no alignment between address
     *               space and arrays)
     * @param banks number of total banks used
     */
    public MemoryModule(int size, int simultaneousBanks, int offset, int banks) {
        numberOfBanks = banks;

        memory = new Word[banks][size];

        initMemory();

        this.offset = offset;
        this.simultaneousBanks = simultaneousBanks;
    }

    /**
     * Responsible for initializing all the Word objects that store each byte of
     * memory
     */
    private void initMemory() {
        Arrays.stream(memory).forEach(row -> Arrays.fill(row, new Word()));
    }

    /**
     * Changes the current active bank
     *
     * @param value to assign to change active bank to
     */
    public void changeActiveBank(int value) {
        activeBank = value;
    }

    /**
     * Method responsible for assigning a value for a specific address in the
     * memory managed by a specific memory module, in this case we need to test
     * various cases, like how many banks exist, how many can be access simultaneously
     * and if both of the responses are more than 1 we need to find in which to
     * write to
     *
     * @param address where memory should be changed
     * @param value   to assign
     */
    @Override
    public void setValue(int address, int value) {
        int realIndex = address - offset;

        if(numberOfBanks == 1)
            memory[activeBank][realIndex].setValue(value);
        else
            if(simultaneousBanks == 1)
                memory[activeBank][realIndex].setValue(value);
            else {
                int moduleSize = memory[activeBank].length;

                if(realIndex >= moduleSize)
                    memory[activeBank][realIndex - moduleSize].setValue(value);
                else
                    memory[0][realIndex].setValue(value);
            }
    }

    /**
     * Method responsible for returning a value for a specific address in the
     * memory managed by a specific memory module, in this case we need to test
     * various cases, like how many banks exist, how many can be access simultaneously
     * and if both of the responses are more than 1 we need to find in which to
     * read from
     *
     * @param address where memory should be retrieved
     * @return value in given address
     */
    @Override
    public int getValue(int address) {
        int realIndex = address - offset;

        if(numberOfBanks == 1)
            return memory[activeBank][realIndex].getValue();
        else
        if(simultaneousBanks == 1)
            return memory[activeBank][realIndex].getValue();
        else {
            int moduleSize = memory[activeBank].length;

            if(realIndex >= moduleSize)
                return memory[activeBank][realIndex - moduleSize].getValue();
            else
                return memory[0][realIndex].getValue();
        }
    }

    /**
     * Method responsible for returning a Word for a specific address in the
     * memory managed by a specific memory module, in this case we need to test
     * various cases, like how many banks exist, how many can be access simultaneously
     * and if both of the responses are more than 1 we need to find in which to
     * write to
     *
     * @param address where memory should be retrieved
     * @return Word (as object) in given address
     */
    @Override
    public Word getWord(int address) {
        int realIndex = address - offset;

        if(numberOfBanks == 1)
            return memory[activeBank][realIndex];
        else
        if(simultaneousBanks == 1)
            return memory[activeBank][realIndex];
        else {
            int moduleSize = memory[activeBank].length;

            if(realIndex >= moduleSize)
                return memory[activeBank][realIndex - moduleSize];
            else
                return memory[0][realIndex];
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return "To Be Implemented";
    }
}
