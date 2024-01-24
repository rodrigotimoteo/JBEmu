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
     * Stores whether the timer is currently enabled
     */
    private boolean timerEnabled;

    /**
     * Stores if there has been a timer overflow that needs handling
     */
    private boolean handleOverflow;

    /**
     * Stores the amount of Machine Cycles executed
     */
    private int machineCycles = 0;

    /**
     * Stores the cycles when halt was called (used for dealing with halt bug)
     */
    private int haltCycleCounter = 0;

    /**
     * Stores the cycle count when interrupt status was changed
     */
    private int interruptChangedCounter = 0;


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
        machineCycles++;

        tickDividerTimer();
        tickNormalTimer();
    }

    private void tickDividerTimer() {

    }

    private void tickNormalTimer() {

    }

    /**
     * Returns the number of machines cycles executed by this instance
     *
     * @return machine cycle count
     */
    public int getMachineCycles() {
        return machineCycles;
    }

    /**
     * Returns the number of cycles when halt was activated
     *
     * @return machine cycle count
     */
    public int getHaltCycleCounter() {
        return haltCycleCounter;
    }

    /**
     * Sets the number of cycles when halt was activated
     */
    public void setHaltCycleCounter() {
        haltCycleCounter = machineCycles;
    }

    /**
     * Getter for the cycle count when interrupts status was last changed
     *
     * @return machine cycle count
     */
    public int getInterruptChangedCounter() {
        return interruptChangedCounter;
    }

    /**
     * Sets the number of cycles when the interrupts status was last changed
     */
    public void setInterruptChangedCounter() {
        this.interruptChangedCounter = machineCycles;
    }
}
