package io.github.ppu;

import io.github.memory.Bus;

/**
 *
 *
 * @author rodrigotimoteo
 */

public class PPU {

    /**
     * Stores a reference to the bus
     */
    private final Bus bus;

    /**
     * Stores the reference to the ppu drawer object
     */
    private final Drawer drawer;

    /**
     * Stores the reference to the ppu registers object
     */
    private final Registers registers;

    /**
     * Stores the reference to the ppu timers object
     */
    private final Timers timers;

    /**
     * Stores whether is running on cgb mode or not
     */
    private final boolean cgb;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public PPU(Bus bus) {
        this.bus = bus;

        cgb = bus.isCgb();

        drawer    = new Drawer(bus, null);
        registers = new Registers(bus);
        timers    = new Timers(bus);
    }

    public void tick() {

    }

}
