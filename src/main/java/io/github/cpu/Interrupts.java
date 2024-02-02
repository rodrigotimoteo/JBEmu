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
    enum InterruptTypes {
        VBLANK_INT(0),
        STAT_INT(1),
        TIMER_INT(2),
        SERIAL_INT(3),
        JOYPAD_INT(4);

        private final int value;

        InterruptTypes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

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
     * Stores whether the CPU is currently reacting to interrupts, disabled by
     * default
     */
    private boolean interruptMasterEnable = false;

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

    /**
     * This method handles everything that deals directly with the execution of
     * CPU Interrupts, as well as testing for the halt bug
     */
    public void handleInterrupt() {
        Word availableInterrupts = decodeServiceableInterrupts();

        if(interruptMasterEnable) {

            if(availableInterrupts.getValue() != 0) {
                bus.executeFromCPU(Bus.UNHALT, null);
                disableIME();

                bus.storePCInSP();

                checkInterruptTypes(availableInterrupts);
            }
        } else if((Boolean) bus.getFromCPU(Bus.GET_HALTED, null)) {
            if(availableInterrupts.getValue() != 0) {
                bus.executeFromCPU(Bus.HALT, null);

                int machineCycles     =
                        (int) bus.getFromCPU(Bus.GET_MC, null);
                int haltMachineCycles =
                        (int) bus.getFromCPU(Bus.GET_HALT_MC, null);

                if(machineCycles == haltMachineCycles)
                    haltBug = true;
            }
        }

    }

    /**
     * Decodes the interrupts being request, this is obtained from the IE and IF
     * register
     *
     * @return value of IE register and IF register after and bit operation
     */
    private Word decodeServiceableInterrupts() {
        return new Word(IE_REGISTER.getValue() & IF_REGISTER.getValue());
    }

    /**
     * Check if a specific interrupt type is ready to be executed if so, reset it
     * in the Interrupt Flags register and return back.
     * The 0x08 multiplication is obtained from the hardware coded PC's from where
     * each interrupt type should jump to, they are curiously 8 address apart from
     * each other.
     *
     * @param availableInterrupts Word containing the register's that are ready
     *                            to be handled
     */
    private void checkInterruptTypes(Word availableInterrupts) {
        for(InterruptTypes interrupt : InterruptTypes.values()) {
            if(availableInterrupts.testBit(interrupt.value)) {
                bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(
                        ReservedAddresses.INTERRUPT_START.getAddress() +
                                0x08 * interrupt.value)});
                IF_REGISTER.resetBit(interrupt.value);

                return;
            }
        }
    }

    /**
     * Request an interrupt based on given value (values corresponds to enum
     * structure at top of this class)
     *
     * @param interrupt which interrupt (bit) to set in IF flag
     */
    public void requestInterrupt(int interrupt) {
        if(interrupt < 0 || interrupt > 4) return;

        IF_REGISTER.setBit(interrupt);
    }

    /**
     * Used to request a new interrupt state change (IME change)
     *
     * @param changeToState which state to change the IME (true if enabling,
     *                      false otherwise)
     */
    public void setInterruptChange(boolean changeToState) {
        interruptChange = true;
        this.changeToState = changeToState;
    }

    /**
     * Checks if a IME change should be performed
     *
     * @return if there is IME change request
     */
    public boolean requestedInterruptChange() {
        return interruptChange;
    }

    /**
     * If we have a requested IME change this performs that operation
     */
    public void triggerIMEChange() {
        interruptMasterEnable = changeToState;
        interruptChange = false;
    }

    public void disableIME() {
        interruptMasterEnable = false;
    }

    /**
     * Checks if the halt bug is currently active
     *
     * @return halt bug state
     */
    public boolean isHaltBug() {
        return haltBug;
    }

    /**
     * Disables the halt bug
     */
    public void disableHaltBug() {
        haltBug = false;
    }
}
