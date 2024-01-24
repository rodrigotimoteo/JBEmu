package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;

/**
 * Class responsible for handling all things that deal with program counter changes
 * as well as stack pointer operations. Jumps, Restarts, Calls, etc
 *
 * @author rodrigotimoteo
 */

public class Jump {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the jump operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Jump(Bus bus) {
        this.bus = bus;
    }

    /**
     * Gets the boolean correspondent to the Condition we are testing (conditions
     * explained in method that use them as parameters)
     *
     * @param conditionString which condition to tet
     * @return result of testing given condition
     */
    private boolean getConditionValue(String conditionString) {
        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        return switch (conditionString) {
            case "NZ" -> !flags.getZeroFlag();
            case "Z"  ->  flags.getZeroFlag();
            case "NC" -> !flags.getCarryFlag();
            case "C"  ->  flags.getCarryFlag();
            default ->
                    throw new IllegalStateException("Unexpected condition " + conditionString);
        };
    }

    /**
     * Executes the operation that jumps to the address on the two words after
     * the program counter current value
     */
    public void jp() {
        int jumpAddress = bus.calculateNN();

        bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(jumpAddress)});
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
    }

    /**
     * Executes the operation that jump to the address on the two words after
     * the program counter but if only the condition given is true
     * "NZ" -> Zero flag is false
     * "Z"  -> Zero flag is true
     * "NC" -> Carry flag is false
     * "C"  -> Carry flag is true
     *
     * @param conditionString which condition to check as String
     */
    public void jpCond(String conditionString) {
        boolean condition = getConditionValue(conditionString);

        if(condition) jp();
        else {
            bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
            bus.executeFromCPU(Bus.TICK_TIMERS, null);
            bus.executeFromCPU(Bus.TICK_TIMERS, null);
        }
    }

    /**
     * Executes the operation that jumps to the address contained in the memory
     * at the HL value
     */
    public void jpHL() {
        int hl = (Integer) bus.getFromCPU(Bus.GET_HL, null);

        bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(hl)});
    }

    /**
     * This operation add a value N to the program counter current address and
     * jumps to it (the value is retrieved from the memory directly after the
     * program counter current address)
     */
    public void jr() {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);

        int check = bus.getValue(programCounter + 1);

        if((check >> 7) == 0)
            bus.executeFromCPU(Bus.INCR_PC, new String[]{String.valueOf(check & 0x7f)});
        else
            bus.executeFromCPU(Bus.INCR_PC, new String[]{String.valueOf((check & 0x7f) - 128)});

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * This operation add a value N to the program counter current address and
     * jumps to it (the value is retrieved from the memory directly after the
     * program counter current address) This only happens if the following
     * condition is true.
     * "NZ" -> Zero flag is false
     * "Z"  -> Zero flag is true
     * "NC" -> Carry flag is false
     * "C"  -> Carry flag is true
     *
     * @param conditionString which condition to check as String
     */
    public void jrCond(String conditionString) {
        boolean condition = getConditionValue(conditionString);

        if(condition) jr();
        else {
            bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
            bus.executeFromCPU(Bus.TICK_TIMERS, null);
        }
    }

    /**
     * This operation pushes the address of the next instruction ot be executed
     * onto the stack and then jumps to the address NN (16bits) directly after
     * the program counter current address
     */
    public void call() {
        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);
        int jumpAddress = bus.calculateNN();
        int tempProgramCounter = programCounter + 3;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(jumpAddress)});
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(stackPointer - 1, (tempProgramCounter & 0xff00) >> 8);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(stackPointer - 2, (tempProgramCounter & 0xff));

        bus.executeFromCPU(Bus.INCR_SP, new String[]{"-2"});
    }

    /**
     * This operation pushes the address of the next instruction ot be executed
     * onto the stack and then jumps to the address NN (16bits) directly after
     * the program counter current address. This happens if the following
     * condition is true
     * "NZ" -> Zero flag is false
     * "Z"  -> Zero flag is true
     * "NC" -> Carry flag is false
     * "C"  -> Carry flag is true
     *
     * @param conditionString which condition to check as String
     */
    public void callCond(String conditionString) {
        boolean condition = getConditionValue(conditionString);

        if(condition) call();
        else {
            bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
            bus.executeFromCPU(Bus.TICK_TIMERS, null);
            bus.executeFromCPU(Bus.TICK_TIMERS, null);
        }
    }

    /**
     * This operation pops two bytes from the stacks and jumps to that address
     */
    public void ret() {
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int lowerAddress = bus.getValue(stackPointer);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int upperAddress = bus.getValue(stackPointer + 1) << 8;

        int returnAddress = upperAddress + lowerAddress;

        bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(returnAddress)});
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_SP, new String[]{"2"});
    }

    /**
     * This operation pops two bytes from the stacks and jumps to that address.
     * This happens if the following condition is true
     * "NZ" -> Zero flag is false
     * "Z"  -> Zero flag is true
     * "NC" -> Carry flag is false
     * "C"  -> Carry flag is true
     *
     * @param conditionString which condition to check as String
     */
    public void retCond(String conditionString) {
        boolean condition = getConditionValue(conditionString);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        if(condition) ret();
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Pops two bytes from the stack and jumps to that address then enables
     * interrupts
     */
    public void reti() {
        ret();

        bus.executeFromCPU(Bus.ENABLE_INT, null);
    }

    /**
     * This operation pushes the present address and jumps to address 0x0000 plus
     * one of the 8 n, where n has 8 possible values, such as
     * n = 0x00
     * n = 0x08
     * n = 0x10
     * n = 0x18
     * n = 0x20
     * n = 0x28
     * n = 0x30
     * n = 0x38
     *
     * @param type defines which n should be used
     */
    public void rst(int type) {
        int address = 0;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        switch (type) {
            case 1 -> address = 0x8;
            case 2 -> address = 0x10;
            case 3 -> address = 0x18;
            case 4 -> address = 0x20;
            case 5 -> address = 0x28;
            case 6 -> address = 0x30;
            case 7 -> address = 0x38;
        }

        int tempProgramCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null) + 1;
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(stackPointer - 1, (tempProgramCounter & 0xff00) >> 8);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(stackPointer - 2, tempProgramCounter & 0xff);

        bus.executeFromCPU(Bus.SET_PC, new String[]{String.valueOf(address)});
        bus.executeFromCPU(Bus.INCR_SP, new String[]{"-2"});
    }

}
