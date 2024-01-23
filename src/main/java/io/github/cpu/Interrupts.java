package io.github.cpu;

import io.github.memory.Bus;
import io.github.memory.ReservedAddresses;
import io.github.memory.Word;

/**
 * Class responsible for handling all the CPU interrupts, these are responsible
 * for servicing hardware timers, such as PPU timers, input and CPU timers
 */

public class Interrupts {

    /**
     * Stores a reference to the bus to communicate with other components
     */
    private final Bus bus;

    /**
     * Stores a reference to the IE register at 0xFFFF (easier access)
     */
    private final Word IE_REGISTER;

    /**
     * Stores a reference to the IF register at 0xFF0F (easier access)
     */
    private final Word IF_REGISTER;

    /**
     * Creates a new Interrupt handler
     *
     * @param bus reference to this instances bus
     */
    public Interrupts(Bus bus) {
        this.bus = bus;

        IE_REGISTER = bus.getWord(ReservedAddresses.IE.getAddress());
        IF_REGISTER = bus.getWord(ReservedAddresses.IF.getAddress());
    }
}
