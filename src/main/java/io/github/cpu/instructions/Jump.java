package io.github.cpu.instructions;

import io.github.memory.Bus;

public class Jump {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the jump operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Jump(Bus bus) {
        this.bus = bus;
    }

}
