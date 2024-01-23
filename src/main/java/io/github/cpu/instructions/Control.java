package io.github.cpu.instructions;

import io.github.memory.Bus;

public class Control {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the cpu control operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Control(Bus bus) {
        this.bus = bus;
    }

    protected void nop() {

    }

}
