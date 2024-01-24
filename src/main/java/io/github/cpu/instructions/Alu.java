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
        if(value1 == 0x00) return 1;
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
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
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

    public void cp(String register) {
        int value = cpuRegisters.getRegister(register).getValue();
        int registerA = cpuRegisters.getRegister("A").getValue();

        if(value == registerA) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        if((value & 0xf) > (registerA & 0xf)) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        if(value > registerA) cpuRegisters.getInternalFlags().setCarryFlag();
        else cpuRegisters.getInternalFlags().resetCarryFlag();

        cpuRegisters.getInternalFlags().setSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void cpSpecial(int address, boolean HL) {
        cpuTimers.handle();

        int value = memoryManager.getValue(address);
        int registerA = cpuRegisters.getRegister("A").getValue();

        if(value == registerA) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        if((value & 0xf) > (registerA & 0xf)) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        if(value > registerA) cpuRegisters.getInternalFlags().setCarryFlag();
        else cpuRegisters.getInternalFlags().resetCarryFlag();

        cpuRegisters.getInternalFlags().setSubtractFlag();

        if(HL)  bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
        else    bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void inc(String register) {
        int value = cpuRegisters.getRegister(register).getValue();

        if((value & 0xf) == 0xf) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        value = (value + 1) & 0xff;

        cpuRegisters.setRegister(register, value);

        if(value == 0) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        cpuRegisters.getInternalFlags().resetSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void incSpecial(int address) {
        cpuTimers.handle();

        int value = memoryManager.getValue(address);

        if((value & 0xf) == 0xf) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        value = (value + 1) & 0xff;

        cpuTimers.handle();
        memoryManager.setValue(address, value);

        if(value == 0) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        cpuRegisters.getInternalFlags().resetSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void dec(String register) {
        int value = cpuRegisters.getRegister(register).getValue();

        if((value & 0xf) == 0) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        value -= 1;

        cpuRegisters.setRegister(register, value);

        if(value == 0) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        cpuRegisters.getInternalFlags().setSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void decSpecial(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int value = bus.getValue(address);

        if((value & 0xf) == 0) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        value -= 1;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        memoryManager.setValue(address, value & 0xff);

        if(value == 0) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();

        cpuRegisters.getInternalFlags().setSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addHL(int register) {
        int HL = cpuRegisters.getHL();
        int R = 0;

        switch(register) {
            case 0 -> R = cpuRegisters.getBC();
            case 1 -> R = cpuRegisters.getDE();
            case 2 -> R = cpuRegisters.getHL();
        }

        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        if(((HL & 0xfff) + (R & 0xfff) & 0x1000) == 0x1000) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        if((HL & 0xffff) + (R & 0xffff) > 0xffff) cpuRegisters.getInternalFlags().setCarryFlag();
        else cpuRegisters.getInternalFlags().resetCarryFlag();

        int temp = ((HL & 0xffff) + (R & 0xffff)) & 0xffff;

        cpuRegisters.setHL(temp);

        cpuRegisters.getInternalFlags().resetSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addHLSP() {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int HL = cpuRegisters.getHL();
        int SP = cpuRegisters.getStackPointer();

        if(((HL & 0xfff) + (SP & 0xfff) & 0x1000) == 0x1000) cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        if((HL & 0xffff) + (SP & 0xffff) > 0xffff) cpuRegisters.getInternalFlags().setCarryFlag();
        else cpuRegisters.getInternalFlags().resetCarryFlag();

        int temp = ((HL & 0xffff) + (SP & 0xffff)) & 0xffff;

        cpuRegisters.setHL(temp);

        cpuRegisters.getInternalFlags().resetSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    public void addSP(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int value = memoryManager.getValue(address);
        int stackPointer = cpuRegisters.getStackPointer();
        if(((value & 0x80) >> 7) == 1) value = (value & 0x7f) - 0x80;

        if((((stackPointer & 0x0f) + (memoryManager.getValue(address) & 0x0f)) & 0x10) == 0x10)
            cpuRegisters.getInternalFlags().setHalfCarryFlag();
        else
            cpuRegisters.getInternalFlags().resetHalfCarryFlag();

        if((((stackPointer & 0xff) + (memoryManager.getValue(address) & 0xff)) & 0x100) == 0x100)
            cpuRegisters.getInternalFlags().setCarryFlag();
        else
            cpuRegisters.getInternalFlags().resetCarryFlag();

        value = (cpuRegisters.getStackPointer() + value) & 0xffff;

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        cpuRegisters.setStackPointer(value);
        cpuRegisters.getInternalFlags().resetZeroFlag();
        cpuRegisters.getInternalFlags().resetSubtractFlag();

        bus.executeFromCPU(Bus.INCR_PC, new String[]{"2"});
    }

    public void incR(int register) {
        switch(register) {
            case 0 -> {
                int temp = cpuRegisters.getBC();
                temp = (temp + 1) & 0xffff;
                cpuRegisters.setBC(temp);
            }
            case 1-> {
                int temp = cpuRegisters.getDE();
                temp = (temp + 1) & 0xffff;
                cpuRegisters.setDE(temp);
            }
            case 2-> {
                int temp = cpuRegisters.getHL();
                temp = (temp + 1) & 0xffff;
                cpuRegisters.setHL(temp);
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
                int temp = cpuRegisters.getBC();
                temp = (temp - 1) & 0xffff;
                cpuRegisters.setBC(temp);
            }
            case 1-> {
                int temp = cpuRegisters.getDE();
                temp = (temp - 1) & 0xffff;
                cpuRegisters.setDE(temp);
            }
            case 2-> {
                int temp = cpuRegisters.getHL();
                temp = (temp - 1) & 0xffff;
                cpuRegisters.setHL(temp);
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

    public void daa() {
        if(cpuRegisters.getInternalFlags().getSubtractFlag()) {
            if(cpuRegisters.getInternalFlags().getCarryFlag())
                cpuRegisters.setRegister("A",
                        (cpuRegisters.getRegister("A").getValue() - 0x60) & 0xff);
            if(cpuRegisters.getInternalFlags().getHalfCarryFlag())
                cpuRegisters.setRegister("A",
                        (cpuRegisters.getRegister("A").getValue() - 0x06) & 0xff);
        } else {
            if(cpuRegisters.getInternalFlags().getCarryFlag() || cpuRegisters.getRegister("A").getValue() > 0x99) {
                cpuRegisters.setRegister("A",
                        (cpuRegisters.getRegister("A").getValue() + 0x60) & 0xff);
                cpuRegisters.getInternalFlags().setCarryFlag();
            }
            if(cpuRegisters.getInternalFlags().getHalfCarryFlag() || (cpuRegisters.getRegister("A").getValue() & 0x0f) > 0x09)
                cpuRegisters.setRegister("A",
                        (cpuRegisters.getRegister("A").getValue() + 0x6) & 0xff);
        }

        if(cpuRegisters.getRegister("A").getValue() == 0) cpuRegisters.getInternalFlags().setZeroFlag();
        else cpuRegisters.getInternalFlags().resetZeroFlag();
        cpuRegisters.getInternalFlags().resetHalfCarryFlag();

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
