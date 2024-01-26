package io.github.cpu;

import io.github.cpu.instructions.Decoder;
import io.github.memory.Bus;
import io.github.memory.Word;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Represents the Central Processing Unit of the emulator an 8-bit 8080-like
 * Sharp CPU
 *
 * @author rodrigotimoteo
 */

public class CPU {

    /**
     * Debug logger
     */
    private PrintStream debug;

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Stores a reference to the cpu registers object
     */
    private final Registers registers;

    /**
     * Stores a reference to the cpu timers object
     */
    private final Timers timers;

    /**
     * Stores a reference to the cpu interrupts object
     */
    private final Interrupts interrupts;

    /**
     * Stores a reference to the cpu decoder object
     */
    private final Decoder decoder;

    /**
     * Indicates whether it's intended to run on Color Game Boy mode or not
     */
    private final boolean cgb;

    /**
     * Stores whether the CPU is currently in halt
     */
    private boolean isHalted = false;

    /**
     * Stores whether the GB is currently stopped
     */
    private boolean isStopped = false;

    /**
     * Initializes a new CPU object with a reference to the bus
     *
     * @param bus reference to this instances bus
     */
    public CPU(Bus bus) throws FileNotFoundException {
        this.bus = bus;

        cgb = bus.isCgb();

        registers   = new Registers(bus);
        timers      = new Timers(bus);
        interrupts  = new Interrupts(bus);
        decoder     = new Decoder(bus);

        //DEBUG USE ONLY
//        if(true) {
//            debug = new PrintStream("test.txt");
//            PrintStream console = System.out;
//            System.setOut(debug);
//        }
    }

    /**
     * Executes a CPU Cycle
     */
    public void tick() {
        //DEBUG USE ONLY
//        System.out.println(registers);

        if(!isStopped()) {
            if(!isHalted()) {
                fetchOperation();

                boolean imeChange = interrupts.requestedInterruptChange();
                int interruptChangeCounter = timers.getInterruptChangedCounter();
                int machineCycles = timers.getMachineCycles();

                if (imeChange && interruptChangeCounter < machineCycles) {
                    interrupts.triggerIMEChange();
                }

            } else
                timers.tick();

            interrupts.handleInterrupt();
        }
    }

    /**
     * Fetches an instruction and gives that instruction for the decoder to
     * decode and execute
     */
    private void fetchOperation() {
        int programCounter = registers.getProgramCounter();

        if(interrupts.isHaltBug()) {
            decoder.decode(programCounter);
            registers.incrementProgramCounter(-1);
            interrupts.disableHaltBug();
        } else {
            decoder.decode(bus.getValue(programCounter));
        }
    }

    /**
     * Checks if the CPU is halted
     *
     * @return halted boolean variable
     */
    public boolean isHalted() {
        return isHalted;
    }

    /**
     * Checks if the GB is stopped
     *
     * @return stopped boolean variable
     */
    public boolean isStopped() {
        return isStopped;
    }

    /**
     * Gives a new boolean value to the variable that checks if CPU is halted
     *
     * @param halted true if halted false otherwise
     */
    public void setHalted(boolean halted) {
        if(halted)
            timers.setHaltCycleCounter();

        isHalted = halted;
    }

    /**
     * Gives a new boolean value to the variable that checks if GB is stopped
     *
     * @param stopped true if stopped false otherwise
     */
    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    /**
     * Changes the interrupt status, this function is called upon when we want
     * to disable or enable interrupts
     *
     * @param changeTo true if we want to enable interrupts false otherwise
     */
    public void interruptChange(boolean changeTo) {
        interrupts.setInterruptChange(changeTo);
        timers.setInterruptChangedCounter();
    }

    /**
     * Registers getter method
     *
     * @return Registers object
     */
    public Registers getRegisters() {
        return registers;
    }

    /**
     * Timers getter method
     *
     * @return Timers object
     */
    public Timers getTimers() {
        return timers;
    }

    /**
     * Interrupts getter method
     *
     * @return Interrupts object
     */
    public Interrupts getInterrupts() {
        return interrupts;
    }
}
