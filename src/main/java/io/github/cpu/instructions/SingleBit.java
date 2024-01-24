package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.Word;

/**
 * Class responsible for handling all things that deal with single bit operations
 * as CPU instructions
 *
 * @author rodrigotimoteo
 */

public class SingleBit {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the single bit operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public SingleBit(Bus bus) {
        this.bus = bus;
    }

    /**
     * Test given bit of a specific register given
     *
     * @param bit to test
     * @param register to test
     */
    protected void bit(int bit, String register) {
        Word cpuRegister = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});

        boolean testResult = cpuRegister.testBit(bit);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        if(testResult)
            flags.setFlags(0, 0, 1, 2);
        else
            flags.setFlags(1, 0, 1, 2);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Test given bit of a specific memory address contained in (HL)
     *
     * @param bit to test
     * @param address retrieve memory to test bit
     */
    protected void bitHL(int bit, int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        boolean testResult = bus.getWord(address).testBit(bit);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        if(testResult)
            flags.setFlags(0, 0, 1, 2);
        else
            flags.setFlags(1, 0, 1, 2);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Sets a bit on the given cpu register
     *
     * @param bit to set
     * @param register to change the given bit
     */
    protected void set(int bit, String register) {
        Word cpuRegister = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});

        cpuRegister.setBit(bit);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the operation that sets a specific bit from a Word retrieved
     * from the main memory
     *
     * @param bit to set
     * @param address to retrieve word from
     */
    protected void setHL(int bit, int address) {
        Word word = bus.getWord(address);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        word.setBit(bit);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Resets a bit on the given cpu register
     *
     * @param bit to reset
     * @param register to change the given bit
     */
    protected void res(int bit, String register) {
        Word cpuRegister = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});

        cpuRegister.resetBit(bit);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the operation that resets a specific bit from a Word retrieved
     * from the main memory
     *
     * @param bit to reset
     * @param address to retrieve word from
     */
    protected void resHL(int bit, int address) {
        Word word = bus.getWord(address);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        word.resetBit(bit);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

}
