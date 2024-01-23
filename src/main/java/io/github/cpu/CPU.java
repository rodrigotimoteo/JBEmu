package io.github.cpu;

import io.github.memory.Bus;

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
    }


}
