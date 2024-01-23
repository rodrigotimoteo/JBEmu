package io.github.cpu.instructions;

import io.github.memory.Bus;

public class RotateShift {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the rotate and shift operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public RotateShift(Bus bus) {
        this.bus = bus;
    }

}
