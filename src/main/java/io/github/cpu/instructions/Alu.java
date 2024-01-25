package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.Word;

/**
 * Class responsible for handling all things that deal with arithmetic operations
 *
 * @author rodrigotimoteo
 */

public class Alu {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the alu operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public Alu(Bus bus) {
        this.bus = bus;
    }

    /**
     * Checks if the half carry flag will be set or reset based on the values
     * used in the arithmetic calculation. Used for additions
     *
     * @param value1 first value to use
     * @param value2 second value to use
     * @param carry use 1 if the calculation uses the carry flag as true, 0
     *              otherwise
     * @return status of half carry flag (1 if true 0 otherwise)
     */
    private int checkHalfCarryAdd(int value1, int value2, int carry) {
        if((((value1 & 0xf) + (value2 & 0xf) + carry) & 0x10) == 0x10)
            return 1;
        else
            return 0;
    }

    /**
     * Checks if the carry flag will be set or reset based on the values used
     * in the arithmetic calculation. Used for additions
     *
     * @param value1 to check
     * @return status of the carry flag (1 if true 0 otherwise)
     */
    private int checkCarryAdd(int value1) {
        if(value1 > 0xFF) return 1;
        else return 0;
    }

    /**
     * Checks if the zero flag will be set or reset based on the values used
     * in the arithmetic calculation. Used for additions
     *
     * @param value1 to check
     * @return status of the zero flag (1 if true 0 otherwise)
     */
    private int checkZero(int value1) {
        if((value1 & 0xFF) == 0x00) return 1;
        else return 0;
    }

    private int checkHalfCarrySub(int value1, int value2, int carry) {
        if(((value1 & 0xf) - (value2 & 0xf) - carry) < 0)
            return 1;
        else
            return 0;
    }

    private int checkCarrySub(int value1) {
        if(value1 < 0) return 1;
        else return 0;
    }

    /**
     * Performs the operations of adding any given value (in this case only the
     * ones contained inside registers) to A
     *
     * @param register used to retrieve the register to add to register A's value
     */
    public void add(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int halfCarry = checkHalfCarryAdd(valueNValue, registerAValue, 0);

        valueNValue += registerAValue;

        int carry = checkCarryAdd(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        flags.setFlags(zero, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Performs the operations of adding any given value (in this case the value
     * is contained in memory) to A. This can be using register address (1 mCycles)
     * or given by the memory directly (2 mCycles)
     *
     * @param address used to retrieve the value to add to register A's value
     * @param HL      if HL is being used or not
     */
    public void addSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int halfCarry = checkHalfCarryAdd(valueNValue, registerAValue, 0);

        valueNValue += registerAValue;

        int carry = checkCarryAdd(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        flags.setFlags(zero, 0, halfCarry, carry);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * Performs the operations of adding any given value (in this case only the
     * ones contained inside registers) to A also adding the carry flag status
     * (1 if true 0 otherwise)
     *
     * @param register used to retrieve the register to add to register A's value
     */
    public void adc(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int carry = flags.getCarryFlag() ? 1 : 0;

        int halfCarry = checkHalfCarryAdd(valueNValue, registerAValue, carry);

        valueNValue += registerAValue + carry;

        carry = checkCarryAdd(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        flags.setFlags(zero, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Performs the operations of adding any given value (in this case the value
     * is contained in memory) to A also adding the carry flag status (1 if true
     * 0 otherwise). This can be using register address (1 mCycles) or given by
     * the memory directly (2 mCycles)
     *
     * @param address used to retrieve the value to add to register A's value
     * @param HL      if HL is being used or not
     */
    public void adcSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int carry = flags.getCarryFlag() ? 1 : 0;

        int halfCarry = checkHalfCarryAdd(valueNValue, registerAValue, carry);

        valueNValue += registerAValue + carry;

        carry = checkCarryAdd(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        flags.setFlags(zero, 0, halfCarry, carry);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }


    public void sub(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, 0);

        valueNValue = (registerAValue - valueNValue);

        int carry = checkCarrySub(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue & 0xFF)});

        flags.setFlags(zero, 1, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void subSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, 0);

        valueNValue = (registerAValue - valueNValue);

        int carry = checkCarrySub(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue & 0xFF)});

        flags.setFlags(zero, 1, halfCarry, carry);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void sbc(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int carry = flags.getCarryFlag() ? 1 : 0;

        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, carry);

        valueNValue = (registerAValue - valueNValue - carry);

        carry = checkCarrySub(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue & 0xFF)});

        flags.setFlags(zero, 1, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void sbcSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int carry = flags.getCarryFlag() ? 1 : 0;

        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, carry);

        valueNValue = (registerAValue - valueNValue - carry);

        carry = checkCarrySub(valueNValue);
        int zero = checkZero(valueNValue);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue & 0xFF)});

        flags.setFlags(zero, 1, halfCarry, carry);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void and(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = registerAValue & valueNValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 1, 0);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void andSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = registerAValue & valueNValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 1, 0);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void or(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = valueNValue | registerAValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 0, 0);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void orSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = valueNValue | registerAValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 0, 0);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void xor(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = valueNValue ^ registerAValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 0, 0);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void xorSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue    = bus.getValue(address);
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        valueNValue = valueNValue ^ registerAValue;

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, 0, 0);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * This operation compares the given register with register A's value,
     * basically does a substitution but throws the results away
     *
     * @param register used to retrieve the register to compare to register A's
     *                 value
     */
    public void cp(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int valueNValue = valueN.getValue();
        int registerAValue = registerA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int zero = checkZero(valueNValue - registerAValue);
        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, 0);
        int carry = checkCarrySub(registerAValue - valueNValue);

        flags.setFlags(zero, 1, halfCarry, carry);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void cpSpecial(int address, boolean HL) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int registerAValue = registerA.getValue();
        int valueNValue = bus.getValue(address);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int zero = checkZero(valueNValue - registerAValue);
        int halfCarry = checkHalfCarrySub(registerAValue, valueNValue, 0);
        int carry = checkCarrySub(registerAValue - valueNValue);

        flags.setFlags(zero, 1, halfCarry, carry);

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    /**
     * This operation increments the given register by one
     *
     * @param register used to retrieve the register to increment
     */
    public void inc(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int valueNValue = valueN.getValue();
        int halfCarry = checkHalfCarryAdd(valueNValue, 1, 0);

        valueNValue = (valueNValue + 1) & 0xFF;

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, halfCarry, 2);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{register, String.valueOf(valueNValue)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void incSpecial(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int valueNValue = bus.getValue(address);

        int halfCarry = checkHalfCarryAdd(valueNValue, 1,0 );

        valueNValue = (valueNValue + 1) & 0xFF;

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 0, halfCarry, 2);

        bus.setValue(address, valueNValue);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void dec(String register) {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        int valueNValue = valueN.getValue();

        int halfCarry = checkHalfCarrySub(valueNValue, 1, 0);

        valueNValue--;

        int zero = checkZero(valueNValue);
        flags.setFlags(zero, 1, halfCarry, 2);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{register, String.valueOf(valueNValue)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void decSpecial(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int valueNValue = bus.getValue(address);

        int halfCarry = checkHalfCarrySub(valueNValue, 1, 0);
        valueNValue -= 1;

        int zero = checkZero(valueNValue);

        flags.setFlags(zero, 1, halfCarry, 2);

        bus.setValue(address, valueNValue & 0xFF);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addHL(int register) {
        int HL = (Integer) bus.getFromCPU(Bus.GET_HL, null);
        int R = 0;

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        switch(register) {
            case 0 -> R = (Integer) bus.getFromCPU(Bus.GET_BC, null);
            case 1 -> R = (Integer) bus.getFromCPU(Bus.GET_DE, null);
            case 2 -> R = (Integer) bus.getFromCPU(Bus.GET_HL, null);
        }

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int halfCarry = (((HL & 0xFFF) + (R & 0xFFF) & 0x1000) == 0x1000) ? 1 : 0;
        int carry = ((HL & 0xFFFF) + (R & 0xFFFF) > 0xFFFF) ? 1 : 0;

        int temp = ((HL & 0xffff) + (R & 0xffff)) & 0xffff;


        flags.setFlags(2, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(temp)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addHLSP() {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int HL = (Integer) bus.getFromCPU(Bus.GET_HL, null);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int halfCarry = (((HL & 0xFFF) + (stackPointer & 0xFFF) & 0x1000) == 0x1000) ? 1 : 0;
        int carry = ((HL & 0xFFFF) + (stackPointer & 0xFFFF) > 0xFFFF) ? 1 : 0;

        int temp = ((HL & 0xFFFF) + (stackPointer & 0xFFFF)) & 0xFFFF;

        flags.setFlags(2, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(temp)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addSP(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int valueSigned = bus.getValue(address);
        int stackPointer = (Integer) bus.getFromCPU(Bus.GET_SP, null);
        if(((valueSigned & 0x80) >> 7) == 1) valueSigned = (valueSigned & 0x7F) - 0x80;

        int halfCarry = checkHalfCarryAdd(value, stackPointer, 0);
        int carry = ((((stackPointer & 0xFF) + (value & 0xFF)) & 0x100) == 0x100) ? 1 : 0;

        valueSigned = (stackPointer + valueSigned) & 0xFFFF;

        flags.setFlags(0, 0, halfCarry, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.SET_SP, new String[]{String.valueOf(valueSigned)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void incR(int register) {
        switch(register) {
            case 0 -> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_BC, null);
                temp = (temp + 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_BC, new String[]{String.valueOf(temp)});
            }
            case 1-> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_DE, null);
                temp = (temp + 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_DE, new String[]{String.valueOf(temp)});
            }
            case 2-> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_HL, null);
                temp = (temp + 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(temp)});
            }
        }

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * This operation increments the stack pointer by one
     */
    public void incSP() {
        int SP = (Integer) bus.getFromCPU(Bus.GET_SP, null);
        SP = (SP + 1) & 0xffff;

        bus.executeFromCPU(Bus.SET_SP, new String[]{String.valueOf(SP)});

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void decR(int register) {
        switch(register) {
            case 0 -> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_BC, null);
                temp = (temp - 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_BC, new String[]{String.valueOf(temp)});
            }
            case 1-> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_DE, null);
                temp = (temp - 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_DE, new String[]{String.valueOf(temp)});
            }
            case 2-> {
                int temp = (Integer) bus.getFromCPU(Bus.GET_HL, null);
                temp = (temp - 1) & 0xFFFF;
                bus.executeFromCPU(Bus.SET_HL, new String[]{String.valueOf(temp)});
            }
        }

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * This operation decrements the stack pointer by one
     */
    public void decSP() {
        int SP = (Integer) bus.getFromCPU(Bus.GET_SP, null);
        SP = (SP - 1) & 0xffff;

        bus.executeFromCPU(Bus.SET_SP, new String[]{String.valueOf(SP)});

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * This operation performs a decimal adjustments to the value in register A,
     * this means getting the correct Binary Coded Decimal for the value in A
     */
    public void daa() {
        Word valueN = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueNValue = valueN.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int offset = 0;
        int carry  = 0;

        if((!flags.getSubtractFlag() && (valueNValue & 0x0F) > 0x09) || flags.getHalfCarryFlag())
            offset |= 0x06;
        if((!flags.getSubtractFlag() && valueNValue > 0x99) || flags.getCarryFlag()) {
            offset |= 0x60;
            carry = 1;
        }

        if(flags.getSubtractFlag())
            valueNValue -= offset;
        else
            valueNValue += offset;

        int zero = checkZero(valueNValue & 0xFF);
        flags.setFlags(zero, 2, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(valueNValue)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * This operation takes the A register and complements it (flips all bits)
     */
    public void cpl() {
        Word registerA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});

        int registerAValue = registerA.getValue();
        registerAValue = (~registerAValue & 0xFF);

        bus.executeFromCPU(Bus.SET_REGISTER, new String[]{"A", String.valueOf(registerAValue)});

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);
        flags.setFlags(2, 1, 1, 2);

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }
}
