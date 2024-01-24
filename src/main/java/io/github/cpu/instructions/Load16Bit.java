package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.Word;

public class Load16Bit {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the 16 bit load operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Load16Bit(Bus bus) {
        this.bus = bus;
    }

    /**
     * Loads a 16bit immediate value after the program counter onto a register
     * set, BC, DE or HL, which is used is defined by the argument
     *
     * @param type selects which register set to use
     */
    public void ld16bit(int type) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int value = bus.calculateNN();
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        switch(type) {
            case 0 -> bus.executeFromCPU(Bus.SET_BC, new String[]{String.valueOf(value)});
            case 1 -> bus.executeFromCPU(Bus.SET_DE, new String[]{String.valueOf(value)});
            case 2 -> bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(value)});
        }

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
    }

    /**
     * Puts the two immediate words after the program counter onto the Stack
     * Pointer
     */
    public void ldSPUU() {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int value = bus.calculateNN();

        bus.executeFromCPU(Bus.SET_SP, new String[]{String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
    }

    /**
     * Put the HL 16bit register into the stack pointer
     */
    public void ldSPHL() {;
        int HL = (Integer) bus.getFromCPU(Bus.GET_HL, null);

        bus.executeFromCPU(Bus.SET_SP, new String[]{String.valueOf(HL)});
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Puts SP plus the immediate word n after the program counter into HL
     */
    public void LDHL() {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);
        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int signedValue = bus.getValue(programCounter + 1);
        if(((signedValue & 0x80) >> 7) == 1)
            signedValue = (signedValue & 0x7F) - 0x80;

        int address = (stackPointer + signedValue) & 0xFFFF;

        int value = bus.getValue(programCounter + 1);

        int halfCarry = (((stackPointer & 0xF) + (value & 0xF) & 0x10) == 0x10) ? 1 : 0;
        int carry = ((((stackPointer & 0xFF) + value) & 0x100) == 0x100) ? 1 : 0;

        bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(address)});
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        flags.setFlags(0, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * Put stack pointer at 16bit address immediately after the program counter
     */
    public void LDnnSP() {
        int address = bus.calculateNN();
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address + 1, (char) ((stackPointer & 0xFF00) >> 8));
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, (char) (stackPointer & 0x00FF));

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
    }

    /**
     * Push register pair given onto the stack. Decrements the Stack Pointer
     * twice
     *
     * @param register pair of register to use as input
     */
    public void push(int register) {
        String in1, in2;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        switch (register) {
            case 0 -> { in1 = "A"; in2 = "F"; }
            case 1 -> { in1 = "B"; in2 = "C"; }
            case 2 -> { in1 = "D"; in2 = "E"; }
            case 3 -> { in1 = "H"; in2 = "L"; }
            default -> { return; }
        }

        Word word1 = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{in1});
        Word word2 = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{in2});
        int value1 = word1.getValue();
        int value2 = word2.getValue();
        int addressLower = stackPointer - 1;
        int addressUpper = stackPointer - 2;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(addressLower, value1);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(addressUpper, value2);

        bus.executeFromCPU(Bus.INCR_SP, new String[]{"-2"});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Pops two bytes off the stack into register pair given by the input
     * variable. Increments the Stack Pointer twice
     *
     * @param register which register pair to use
     */
    public void pop(int register) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        int wordUpper = bus.getValue(stackPointer + 1) << 8;
        int wordLower = bus.getValue(stackPointer);

        int word16Bit = wordUpper + wordLower;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        switch(register) {
            case 0 -> bus.executeFromCPU(Bus.SET_AF, new String[]{String.valueOf(word16Bit)});
            case 1 -> bus.executeFromCPU(Bus.SET_BC, new String[]{String.valueOf(word16Bit)});
            case 2 -> bus.executeFromCPU(Bus.SET_DE, new String[]{String.valueOf(word16Bit)});
            case 3 -> bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(word16Bit)});
        }

        bus.executeFromCPU(Bus.INCR_SP, new String[]{"2"});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }
}
