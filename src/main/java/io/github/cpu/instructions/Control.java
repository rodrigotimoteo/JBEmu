package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.ReservedAddresses;

/**
 * Class responsible for handling all things that deal with special CPU and GB
 * control instructions
 *
 * @author rodrigotimoteo
 */

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

    /**
     * Executes the operation NOP (no operation performed)
     */
    protected void nop() {
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the CCF operation, complements the Carry Flag
     */
    protected void ccf() {
        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        if(flags.getCarryFlag())
            flags.setFlags(2, 0, 0, 0);
        else
            flags.setFlags(2, 0, 0, 1);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the SCF operation, sets the carry flag
     */
    protected void scf() {
        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        flags.setFlags(2, 0, 0, 1);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the HALT operation, powers down the CPU until an interrupt
     * occurs. Used to reduce energy consumption
     */
    protected void halt() {
        bus.executeFromCPU(Bus.HALT, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the STOP operation, halts the CPU and LCD until a button is
     * pressed
     */
    protected void stop() {
        bus.executeFromCPU(Bus.STOP, null);

        bus.setValue(ReservedAddresses.DIV.getAddress(), 0);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the DI operation, responsible for disabling interrupts (but not
     * immediately, only after this instruction's execution)
     */
    protected void di() {
        bus.executeFromCPU(Bus.DISABLE_INT, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Executes the EI operation, responsible for enabling interrupts (but not
     * immediately, only after this instruction's execution)
     */
    protected void ei() {
        bus.executeFromCPU(Bus.ENABLE_INT, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

}
