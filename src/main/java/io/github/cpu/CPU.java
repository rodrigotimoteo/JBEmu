package io.github.cpu;

import io.github.memory.Bus;
import io.github.memory.Word;

/**
 * Represents the Central Processing Unit of the emulator an 8-bit 8080-like
 * Sharp CPU
 *
 * @author rodrigotimoteo
 */

public class CPU {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Stores a reference to the cpu registers object
     */
    private final Registers registers;

    /**
     * Stores a reference to the cpu timers object
     */
    private final Timers timers;

    /**
     * Indicates whether it's intended to run on Color Game Boy mode or not
     */
    private final boolean cgb;

    /**
     * Initializes a new CPU object with a reference to the bus
     *
     * @param bus reference to this instances bus
     */
    public CPU(Bus bus) {
        this.bus = bus;

        cgb = bus.isCgb();

        registers = new Registers(bus);
        timers = new Timers(bus);
    }

    /**
     * Executes a CPU Cycle
     */
    public void tick() {

    }

    /**
     * Gets the register from cpu, retrieving the memory object
     *
     * @param register which register to retrieve
     * @return Word containing the asked register
     */
    public Word getRegister(String register) {
        return registers.getRegister(register);
    }

    public void tickTimers() {
        timers.tick();
    }

}
