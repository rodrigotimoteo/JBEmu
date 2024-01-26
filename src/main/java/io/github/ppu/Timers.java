package io.github.ppu;

import io.github.memory.Bus;

/**
 *
 *
 * @author rodrigotimoteo
 */

public class Timers {

    /**
     *
     */
    private final Bus bus;

    private int tickCounter = 0;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Timers(Bus bus) {
        this.bus = bus;
    }

    public void tick() {
        tickCounter++;
    }

    public int getCounter() {
        return tickCounter;
    }
}
