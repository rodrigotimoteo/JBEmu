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
     * Stores the types of interrupts that can be triggered
     */
    public static final int VBLANK_INTERRUPT = 0;
    public static final int STAT_INTERRUPT   = 1;
    public static final int TIMER_INTERRUPT  = 2;
    public static final int SERIAL_INTERRUPT = 3;
    public static final int JOYPAD_INTERRUPT = 4;

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
     * Stores whether the CPU is currently reacting to interrupts
     */
    private boolean interruptMasterEnable;

    /**
     * Stores a test for the bug that exists on the halt mode of the cpu, that
     * if the interrupt master enable is inactive and the value of the IE
     * register and IF register with and operation is different then 0 the
     * instruction ends and the PC fails to be incremented
     */
    private boolean haltBug;

    /**
     * Stores whether an interrupt state change (enabling or disabling) is
     * queried
     */
    private boolean interruptChange;

    /**
     * If changing interrupt state, tells which state to use (if asked to enable
     * or disabling)
     */
    private boolean changeToState;

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

    public void handleInterrupt() {

    }

    public void setInterruptChange(boolean changeToState) {
        interruptChange = true;
        this.changeToState = changeToState;
    }
}
