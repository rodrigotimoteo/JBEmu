package io.github.cpu.instructions;

import io.github.memory.Bus;

public class Load8Bit {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the 8 bit load operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Load8Bit(Bus bus) {
        this.bus = bus;
    }
}
