package io.github.cpu;

import io.github.memory.Bus;

/**
 * Class purposed with handling everything that needs timings inside the CPU
 * total Cycles, interrupts and others
 *
 * @author rodrigotimoteo
 */

public class Timers {

    /**
     * Stores a reference to the bus to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a new Timer handler object
     *
     * @param bus reference to this instances bus
     */
    public Timers(Bus bus) {
        this.bus = bus;
    }

    /**
     * Advances general timers by one unit
     */
    public void tick() {

    }

}
