package io.github.cpu.instructions;

import io.github.cpu.Flags;
import io.github.memory.Bus;
import io.github.memory.Word;

public class RotateShift {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Creates a handler for the rotate and shift operations done by the CPU
     *
     * @param bus reference to this instances bus
     */
    public RotateShift(Bus bus) {
        this.bus = bus;
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

    /**
     * Rotates A to the left and the bit 7 is used to set or reset the Carry flag
     */
    public void rlca() {
        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueA = wordA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carry = ((valueA & 0x80) == 0x80) ? 1 : 0;

        int carryTemp = (valueA & 0x80) >> 7;
        valueA = ((valueA << 1) & 0xff) | carryTemp;

        flags.setFlags(0, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueA)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates A to the left through the carry flag
     */
    public void rla() {
        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueA = wordA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((valueA & 0x80) == 0x80) ? 1 : 0;

        valueA = ((valueA << 1) & 0xff) | carryTemp;

        flags.setFlags(0, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueA)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotate A to the right and the 0 bit is used to set or reset the carry flag
     */
    public void rrca() {
        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueA = wordA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = (valueA & 0x01) << 7;
        int carry = ((valueA & 0x01) == 0x01) ? 1 : 0;

        valueA = ((valueA >> 1) & 0xff) | carryTemp;
        flags.setFlags(0, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueA)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates A to the right through the carry flag
     */
    public void rra() {
        Word wordA = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{"A"});
        int valueA = wordA.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((valueA & 0x01) == 0x01) ? 1 : 0;

        valueA = (((valueA >> 1) & 0xff) | (carryTemp << 7)) & 0xff;

        flags.setFlags(0, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{"A", String.valueOf(valueA)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates word n to left, and old 7 bit is used to set or reset the carry flag
     *
     * @param register which register to rotate left
     */
    public void rlc(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = (value & 0x80) >> 7;
        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (((value << 1) & 0xFF) | carryTemp) & 0xFF;

        int zero = checkZero(value);
        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }


    /**
     * Rotates given word to the left and uses old 7 bit to set or reset the
     * carry flag
     *
     * @param address HL value
     */
    public void rlcHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carryTemp = (value & 0x80) >> 7;
        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (((value << 1) & 0xFF) | carryTemp) & 0xFF;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, value);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotate given register left through the carry left
     *
     * @param register which register to rotate left
     */
    public void rl(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (((value << 1) & 0xff) | carryTemp) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates given word to the left through carry flag
     *
     * @param address HL value
     */
    public void rlHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (((value << 1) & 0xff) | carryTemp) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, value);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates word n to right, and old 0 bit is used to set or reset the carry
     * flag
     *
     * @param register which register to rotate right
     */
    public void rrc(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = (value & 0x01) << 7;
        int carry = ((value & 0x01) == 0x01) ? 1 : 0;

        value = (((value >> 1) & 0xff) | carryTemp) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates given word to the right and uses old 7 bit to set or reset the
     * carry flag
     *
     * @param address HL value
     */
    public void rrcHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carryTemp = (value & 0x01) << 7;
        int carry = ((value & 0x01) == 0x01) ? 1 : 0;

        value = (((value >> 1) & 0xff) | carryTemp) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, value);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates word n to right through the carry flag
     *
     * @param register which register to rotate right
     */
    public void rr(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((value & 0x01) == 0x01) ? 1 : 0;

        value = (((value >> 1) & 0xff) | (carryTemp << 7)) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Rotates given word to the right through the carry flag
     *
     * @param address HL value
     */
    public void rrHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carryTemp = flags.getCarryFlag() ? 1 : 0;
        int carry = ((value & 0x01) == 0x01) ? 1 : 0;

        value = (((value >> 1) & 0xff) | (carryTemp << 7)) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, value);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts the register's value left into the carry.
     *
     * @param register which register to shift left
     */
    public void sla(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (value << 1) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts word n to left through the carry flag
     *
     * @param address HL value
     */
    public void slaHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carry = ((value & 0x80) == 0x80) ? 1 : 0;

        value = (value << 1) & 0xff;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.setValue(address, value);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Swaps the lower bits with the higher bits of a given register
     *
     * @param register which register to swap
     */
    public void swap(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int lower = (value & 0xF0) >> 4;
        int upper = (value & 0x0F) << 4;

        value = (upper | lower) & 0xFF;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, 0);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Swaps the lower bits with the higher bits of memory stored at the address
     * given by the HL value
     *
     * @param address HL value
     */
    public void swapHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        int value = bus.getValue(address);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int lower = (value & 0xF0) >> 4;
        int upper = (value & 0x0F) << 4;

        value = (upper | lower) & 0xFF;

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, 0);

        bus.setValue(address, value);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts the value contained inside the given register to the right into
     * the carry. Uses the carry value
     *
     * @param register which register to shift
     */
    public void sra(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carryTemp = value & 0x80;
        int carry = ((value & 0x01) != 0) ? 1 : 0;

        value = ((value >> 1) | carryTemp) & 0xFF;

        int zero = checkZero(value);
        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts right the value of the memory contained inside the address given
     * by the 16bit register HL. Uses the carry value
     *
     * @param address HL value
     */
    public void sraHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carryTemp = value & 0x80;
        int carry = ((value & 0x01) != 0) ? 1 : 0;

        value = ((value >> 1) | carryTemp) & 0xFF;

        int zero = checkZero(value);
        flags.setFlags(zero, 0, 0, carry);

        bus.setValue(address, value);
        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts the value contained inside the given register to the right into
     * the carry. Doesn't use the carry value
     *
     * @param register which register to shift
     */
    public void srl(String register) {
        Word word = (Word)
                bus.getFromCPU(Bus.GET_REGISTER, new String[]{register});
        int value = word.getValue();

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int carry = ((value & 0x01) == 0x01) ? 1 : 0;
        value = (value >> 1) & 0xFF;

        int zero = checkZero(value);
        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.SET_REGISTER,new String[]{register, String.valueOf(value)});
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

    /**
     * Shifts right a word contained in the memory address of 16bit register HL into
     * the carry. Doesn't use the carry value
     *
     * @param address HL value
     */
    public void srlHL(int address) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        Flags flags = (Flags) bus.getFromCPU(Bus.GET_FLAGS, null);

        int value = bus.getValue(address);
        int carry = ((value & 0x01) == 0x01) ? 1 : 0;

        value = (value >> 1) & 0xFF;
        bus.setValue(address, value);

        int zero = checkZero(value);

        flags.setFlags(zero, 0, 0, carry);

        bus.executeFromCPU(Bus.TICK_TIMERS, null);
        bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
    }

}
