package io.github.cpu.instructions;

import io.github.memory.Bus;
import io.github.memory.Word;

/**
 * Class responsible for handling all things that deal with 8 Bit loads into
 * registers or memory
 *
 * @author rodrigotimoteo
 */

public class Load8Bit {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the 8 bit load operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Load8Bit(Bus bus) {
        this.bus = bus;
    }

    /**
     * Returns one of the 4 types of combined register in the GB CPU, AF, BC, DE
     * and HL
     *
     * @param number code for which 16 Bit register to return
     * @return value of the 16 bit combined register
     */
    private int decodeRegister(int number) {
        return switch (number) {
            case 0 -> (Integer) bus.getFromCPU(Bus.GET_BC, null);
            case 1 -> (Integer) bus.getFromCPU(Bus.GET_DE, null);
            case 2 -> (Integer) bus.getFromCPU(Bus.GET_HL, null);
            default ->
                    throw new IllegalStateException("Unexpected register " + number);
        };
    }

    /**
     * This operation assigns the value stored in register A to the memory at
     * the location of a 16 Bit Register (combination of register)
     *
     * @param register code for which combination of register to use
     */
    public void ldTwoRegisters(int register) {
        int address = decodeRegister(register);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueRegisterA = registerA.getValue();

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, valueRegisterA);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * This operation assigns the value stored in register A to the memory at the
     * location given by the word next to the program counter in memory
     */
    public void ldNN() {
        int address = bus.calculateNN();

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueRegisterA = registerA.getValue();

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, valueRegisterA);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
    }

    /**
     * Puts the value n (retrieved from the memory address of a 16 Bit register
     * value) into A
     *
     * @param register code for which combination of register to use
     */
    public void ldTwoRegistersIntoA(int register) {
        int address = decodeRegister(register);
        int valueAtAddress = bus.getValue(address);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueAtAddress)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Puts the value nn (retrieved by getting the two values next to the program
     * counter and retrieving the value at the given address) into A
     */
    public void ldNNIntoA() {
        int address = bus.calculateNN();
        int valueAtAddress = bus.getValue(address);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueAtAddress)});

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"3"});
    }

    /**
     * Stores the value given by the immediate word next to the program counter
     * into the given register
     *
     * @param register where to store immediate word
     */
    public void ldNRegister(String register) {
        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);
        int word = bus.getValue(programCounter + 1);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(word)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * Stores the value given by the immediate word next to the program counter
     * into the memory address given by the HL register aggregation
     */
    public void ldNHL() {
        int HL = decodeRegister(2);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);
        int value = bus.getValue(programCounter + 1);

        bus.setValue(HL, value);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * Loads the value in one register into another
     *
     * @param registerIn to receive a new value
     * @param registerOut input register
     */
    public void ld(String registerIn, String registerOut) {
        Word wordOut = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{registerOut});

        int valueOut = wordOut.getValue();

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{registerIn, String.valueOf(valueOut)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Assigns a value contained in the memory address given by the HL register
     * to a given register
     *
     * @param register to receive new value
     */
    public void ldHLtoRegister(String register) {
        int HL = decodeRegister(2);
        int valueHL = bus.getValue(HL);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{register, String.valueOf(valueHL)});

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Assigns a value contained in a register to the memory address given by
     * the HL address
     *
     * @param register to be used as input value
     */
    public void ldRtoHL(String register) {
        Word wordInput = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int valueIn = wordInput.getValue();
        int HL = decodeRegister(2);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        bus.setValue(HL, valueIn);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Assigns a value to A to given word (started from address 0xFF00 added the
     * value of register C) or vice-versa
     *
     * @param AtoC defines whether the value should be assigned to the A register
     *             (false) or otherwise (true)
     */
    public void ldAC(boolean AtoC) {
        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        Word wordC = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"C"});

        int valueA = wordA.getValue();
        int valueC = wordC.getValue();
        int address = 0xFF00 + valueC;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        if(AtoC) bus.setValue(address, valueA);
        else     bus.executeFromCPU(Bus.SET_REGISTER,
                new String[]{"A", String.valueOf(bus.getValue(address))});

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Loads the value from A into the address given by HL or vice-versa as well
     * as lowering HL value by one
     *
     * @param AtoHL sets whether register A should be used as input or has the
     *              receiver
     */
    public void ldd(boolean AtoHL) {
        int HL = decodeRegister(2);

        if(AtoHL) ldTwoRegisters(2);
        else ldTwoRegistersIntoA(2);

        HL = (HL - 1) & 0xffff;
        bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(HL)});
    }

    /**
     * Loads the value from A into the address given by HL or vice-versa as well
     * as incrementing HL value by one
     *
     * @param AtoHL sets whether register A should be used as input (true) or
     *              as the receiver (false)
     */
    public void ldi(boolean AtoHL) {
        int HL = decodeRegister(2);

        if(AtoHL) ldTwoRegisters(2);
        else ldTwoRegistersIntoA(2);

        HL = (HL + 1) & 0xffff;

        bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(HL)});
    }

    /**
     * Puts register A's value into memory address 0xFF00 plus the immediate word
     * after the program counter or vice-versa
     *
     * @param Ato sets whether register A should be used as input (true) or
     *            as the receiver (false)
     */
    public void ldh(boolean Ato) {
        int programCounter = (Integer) bus.getFromCPU(Bus.GET_PC, null);
        int valueN = bus.getValue(programCounter + 1);

        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueA = wordA.getValue();
        int address = 0xFF00 + valueN;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        if(Ato) bus.setValue(address, valueA);
        else    bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueA)});

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

}
