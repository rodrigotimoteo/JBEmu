package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.Word;

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

    /** bit method
     * <p>
     *
     * </p>
     *
     * @param bit
     * @param register
     */
    public void bit(int bit, String register) {


        boolean test = cpuRegisters.getRegister(register).testBit(bit);

        if(!test)
            cpuRegisters.getInternalFlags().setZeroFlag();
        else
            cpuRegisters.getInternalFlags().resetZeroFlag();
        cpuRegisters.getInternalFlags().resetSubtractFlag();
        cpuRegisters.getInternalFlags().setHalfCarryFlag();

        cpuRegisters.incrementProgramCounter(1);
    }

    /**
     * Test given bit of a specific memory address contained in (HL)
     *
     * @param bit
     * @param address
     */
    public void bitHL(int bit, int address) {
        bus.tickCpuTimers();

        boolean testResult = bus.getWord(address).testBit(bit);

        Flags flags = bus.getRegister("F")

        if(!test)
            cpuRegisters.getInternalFlags().setZeroFlag();
        else
            cpuRegisters.getInternalFlags().resetZeroFlag();
        cpuRegisters.getInternalFlags().resetSubtractFlag();
        cpuRegisters.getInternalFlags().setHalfCarryFlag();

        cpuRegisters.incrementProgramCounter(1);
    }

    /**
     * Sets the given register, the given bit
     *
     * @param bit to set
     * @param register to change the given bit
     */
    public void set(int bit, String register) {
        Word cpuRegister = bus.getRegister(register);

        cpuRegister.setBit(bit);

        cpuRegisters.incrementProgramCounter(1);
    }

    /** setHL method
     * <p>
     *
     * </p>
     *
     * @param bit
     * @param address
     */
    public void setHL(int bit, int address) {
        cpuTimers.handle();
        memoryManager.getByte(address).setBit(bit);
        cpuTimers.handle();

        cpuRegisters.incrementProgramCounter(1);
    }

    /** res method
     * <p>
     *
     * </p>
     *
     * @param bit
     * @param register
     */
    public void res(int bit, String register) {
        cpuRegisters.getRegister(register).resetBit(bit);

        cpuRegisters.incrementProgramCounter(1);
    }

    /** resHL method
     * <p>
     *
     * </p>
     *
     * @param bit
     * @param address
     */
    public void resHL(int bit, int address) {
        cpuTimers.handle();
        memoryManager.getByte(address).resetBit(bit);
        cpuTimers.handle();

        cpuRegisters.incrementProgramCounter(1);
    }

}
