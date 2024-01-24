package io.github.cpu.instructions;

import io.github.memory.Bus;

/**
 * This class is responsible for the translation between opcode values and the
 * instructions they represent, for each specific type of instructions there are
 * subclasses to make implementation easier
 *
 * @author rodrigotimoteo
 */

public class Decoder {

    /**
     * Stores a reference to the bus used to communicate with other components
     */
    private final Bus bus;

    /**
     * Holds reference for Alu instruction handler
     */
    private final Alu alu;

    /**
     * Holds reference for Cpu Control instruction handler
     */
    private final Control control;

    /**
     * Holds reference for Jump instruction handler
     */
    private final Jump jump;

    /**
     * Holds reference for 8 Bit Loads instruction handler
     */
    private final Load8Bit load8Bit;

    /**
     * Holds reference for 16 Bit Loads instruction handler
     */
    private final Load16Bit load16Bit;

    /**
     * Holds reference for Rotates and Shifts instruction handler
     */
    private final RotateShift rotateShift;

    /**
     * Holds reference for Single Bit instruction handler
     */
    private final SingleBit singleBit;

    /**
     * Stores whether the next instruction is of the secondary opcode table
     * prefixed by 0xCB
     */
    private boolean CB = false;

    /**
     * Creates a new decoder for a given instance of the bus as well as helper
     * classes for instruction handling
     *
     * @param bus reference to this instances bus
     */
    public Decoder(Bus bus) {
        this.bus = bus;

        alu         = new Alu(bus);
        control     = new Control(bus);
        jump        = new Jump(bus);
        load8Bit    = new Load8Bit(bus);
        load16Bit   = new Load16Bit(bus);
        rotateShift = new RotateShift(bus);
        singleBit   = new SingleBit(bus);
    }

    /**
     * Decodes operation code to be executed and handles the operation to the
     * helper classes
     *
     * @param operationCode to be executed
     */
    public void decode(int operationCode) {
        bus.executeFromCPU(Bus.TICK_TIMERS, null);

        if(!CB)
            handleRegularOPs(operationCode);
        else {
            handleCBOps(operationCode);
            CB = false;
        }
    }

    /**
     * Method that is responsible for attributing a handler for each specific
     * opcode, as well as its main arguments
     *
     * @param operationCode to be executed
     */
    private void handleRegularOPs(int operationCode) {
        switch (operationCode) {
            case 0x00 -> //NOP
                    control.nop();
            case 0x01 -> //LD BC,u16
                    load16Bit.ld16bit(0);
            case 0x02 -> //LD (BC),A
                    load8Bit.ldTwoRegisters(0);
            case 0x03 -> //INC BC
                    alu.incR(0);
            case 0x04 -> //INC B
                    alu.inc("B");
            case 0x05 -> //DEC B
                    alu.dec("B");
            case 0x06 -> //LD B,u8
                    load8Bit.ldNRegister("B");
            case 0x07 -> //RLCA
                    rotateShift.rlca();
            case 0x08 -> //LD (u16),SP
                    load16Bit.LDnnSP();
            case 0x09 -> //ADD HL,BC
                    alu.addHL(0);
            case 0x0A -> //LD A,(BC)
                    load8Bit.ldTwoRegistersIntoA(0);
            case 0x0B -> //DEC BC
                    alu.decR(0);
            case 0x0C -> //INC C
                    alu.inc("C");
            case 0x0D -> //DEC C
                    alu.dec("C");
            case 0x0E -> //LD C,u8
                    load8Bit.ldNRegister("C");
            case 0x0F -> //RRCA
                    rotateShift.rrca();
            case 0x10 -> //STOP
                    control.stop();
            case 0x11 -> //LD DE,u16
                    load16Bit.ld16bit(1);
            case 0x12 -> //LD (DE),A
                    load8Bit.ldTwoRegisters(1);
            case 0x13 -> //INC DE
                    alu.incR(1);
            case 0x14 -> //INC D
                    alu.inc("D");
            case 0x15 -> //DEC D
                    alu.dec("D");
            case 0x16 -> //LD D,u8
                    load8Bit.ldNRegister("D");
            case 0x17 -> //RLA
                    rotateShift.rla();
            case 0x18 -> //JR i8
                    jump.jr();
            case 0x19 -> //ADD HL,DE
                    alu.addHL(1);
            case 0x1A -> //LD A,(DE)
                    load8Bit.ldTwoRegistersIntoA(1);
            case 0x1B -> //DEC DE
                    alu.decR(1);
            case 0x1C -> //INC E
                    alu.inc("E");
            case 0x1D -> //DEC E
                    alu.dec("E");
            case 0x1E -> //LD E,u8
                    load8Bit.ldNRegister("E");
            case 0x1F -> //RRA
                    rotateShift.rra();
            case 0x20 -> //JR NZ,i8
                    jump.jrCond("NZ");
            case 0x21 -> //LD HL,u16
                    load16Bit.ld16bit(2);
            case 0x22 -> //LDI (HL),A
                    load8Bit.ldi(true);
            case 0x23 -> //INC HL
                    alu.incR(2);
            case 0x24 -> //INC H
                    alu.inc("H");
            case 0x25 -> //DEC H
                    alu.dec("H");
            case 0x26 -> //LD H,u8
                    load8Bit.ldNRegister("H");
            case 0x27 -> //DAA
                    alu.daa();
            case 0x28 -> //JR Z,u8
                    jump.jrCond("Z");
            case 0x29 -> //ADD HL, HL
                    alu.addHL(2);
            case 0x2A -> //LDI A,(HL)
                    load8Bit.ldi(false);
            case 0x2B -> //DEC HL
                    alu.decR(2);
            case 0x2C -> //INC L
                    alu.inc("L");
            case 0x2D -> //DEC L
                    alu.dec("L");
            case 0x2E -> //LD L,u8
                    load8Bit.ldNRegister("L");
            case 0x2F -> //CPL
                    alu.cpl();
            case 0x30 -> //JR NC,u8
                    jump.jrCond("NC");
            case 0x31 -> //LD SP,u16
                    load16Bit.ldSPUU();
            case 0x32 -> //LDD (HL),A
                    load8Bit.ldd(true);
            case 0x33 -> //INC SP
                    alu.incSP();
            case 0x34 -> //INC (HL)
                    alu.incSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x35 -> //INC (HL)
                    alu.decSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x36 -> //LD (HL), n
                    load8Bit.ldNHL();
            case 0x37 -> //SCF
                    control.scf();
            case 0x38 -> //JR C,u8
                    jump.jrCond("C");
            case 0x39 -> //ADD HL,SP
                    alu.addHLSP();
            case 0x3A -> //LDD A,(HL)
                    load8Bit.ldd(false);
            case 0x3B -> //DEC SP
                    alu.decSP();
            case 0x3C -> //INC A
                    alu.inc("A");
            case 0x3D -> //DEC A
                    alu.dec("A");
            case 0x3E -> //LD A,u8
                    load8Bit.ldNRegister("A");
            case 0x3F -> //CCF
                    control.ccf();
            case 0x40 -> //LD B,B
                    load8Bit.ld("B", "B");
            case 0x41 -> //LD B,C
                    load8Bit.ld("B", "C");
            case 0x42 -> //LD B,D
                    load8Bit.ld("B", "D");
            case 0x43 -> //LD B,E
                    load8Bit.ld("B", "E");
            case 0x44 -> //LD B,H
                    load8Bit.ld("B", "H");
            case 0x45 -> //LD B,L
                    load8Bit.ld("B", "L");
            case 0x46 -> //LD B,(HL)
                    load8Bit.ldHLtoRegister("B");
            case 0x47 -> //LD B,A
                    load8Bit.ld("B", "A");
            case 0x48 -> //LD C,B
                    load8Bit.ld("C", "B");
            case 0x49 -> //LD C,C
                    load8Bit.ld("C", "C");
            case 0x4A -> //LD C,D
                    load8Bit.ld("C", "D");
            case 0x4B -> //LD C,E
                    load8Bit.ld("C", "E");
            case 0x4C -> //LD C,H
                    load8Bit.ld("C", "H");
            case 0x4D -> //LD C,L
                    load8Bit.ld("C", "L");
            case 0x4E -> //LD C,(HL)
                    load8Bit.ldHLtoRegister("C");
            case 0x4F -> //LD C,A
                    load8Bit.ld("C", "A");
            case 0x50 -> //LD D,B
                    load8Bit.ld("D", "B");
            case 0x51 -> //LD D,C
                    load8Bit.ld("D", "C");
            case 0x52 -> //LD D,D
                    load8Bit.ld("D", "D");
            case 0x53 -> //LD D,E
                    load8Bit.ld("D", "E");
            case 0x54 -> //LD D,H
                    load8Bit.ld("D", "H");
            case 0x55 -> //LD D,L
                    load8Bit.ld("D", "L");
            case 0x56 -> //LD D,(HL)
                    load8Bit.ldHLtoRegister("D");
            case 0x57 -> //LD D,A
                    load8Bit.ld("D", "A");
            case 0x58 -> //LD E,B
                    load8Bit.ld("E", "B");
            case 0x59 -> //LD E,C
                    load8Bit.ld("E", "C");
            case 0x5A -> //LD E,D
                    load8Bit.ld("E", "D");
            case 0x5B -> //LD E,E
                    load8Bit.ld("E", "E");
            case 0x5C -> //LD E,H
                    load8Bit.ld("E", "H");
            case 0x5D -> //LD E,L
                    load8Bit.ld("E", "L");
            case 0x5E -> //LD E,(HL)
                    load8Bit.ldHLtoRegister("E");
            case 0x5F -> //LD E,A
                    load8Bit.ld("E", "A");
            case 0x60 -> //LD H,B
                    load8Bit.ld("H", "B");
            case 0x61 -> //LD H,C
                    load8Bit.ld("H", "C");
            case 0x62 -> //LD H,D
                    load8Bit.ld("H", "D");
            case 0x63 -> //LD H,E
                    load8Bit.ld("H", "E");
            case 0x64 -> //LD H,H
                    load8Bit.ld("H", "H");
            case 0x65 -> //LD H,L
                    load8Bit.ld("H", "L");
            case 0x66 -> //LD H,(HL)
                    load8Bit.ldHLtoRegister("H");
            case 0x67 -> //LD H,A
                    load8Bit.ld("H", "A");
            case 0x68 -> //LD L,B
                    load8Bit.ld("L", "B");
            case 0x69 -> //LD L,C
                    load8Bit.ld("L", "C");
            case 0x6A -> //LD L,D
                    load8Bit.ld("L", "D");
            case 0x6B -> //LD L,E
                    load8Bit.ld("L", "E");
            case 0x6C -> //LD L,H
                    load8Bit.ld("L", "H");
            case 0x6D -> //LD L,L
                    load8Bit.ld("L", "L");
            case 0x6E -> //LD L,(HL)
                    load8Bit.ldHLtoRegister("L");
            case 0x6F -> //LD L,A
                    load8Bit.ld("L", "A");
            case 0x70 -> //LD (HL),B
                    load8Bit.ldRtoHL("B");
            case 0x71 -> //LD (HL),C
                    load8Bit.ldRtoHL("C");
            case 0x72 -> //LD (HL),D
                    load8Bit.ldRtoHL("D");
            case 0x73 -> //LD (HL),E
                    load8Bit.ldRtoHL("E");
            case 0x74 -> //LD (HL),H
                    load8Bit.ldRtoHL("H");
            case 0x75 -> //LD (HL),L
                    load8Bit.ldRtoHL("L");
            case 0x76 -> //HALT
                    control.halt();
            case 0x77 -> //LD (HL),A
                    load8Bit.ldTwoRegisters(2);
            case 0x78 -> //LD A,B
                    load8Bit.ld("A", "B");
            case 0x79 -> //LD A,C
                    load8Bit.ld("A", "C");
            case 0x7A -> //LD A,D
                    load8Bit.ld("A", "D");
            case 0x7B -> //LD A,E
                    load8Bit.ld("A", "E");
            case 0x7C -> //LD A,H
                    load8Bit.ld("A", "H");
            case 0x7D -> //LD A,L
                    load8Bit.ld("A", "L");
            case 0x7E -> //LD A,(HL)
                    load8Bit.ldTwoRegistersIntoA(2);
            case 0x7F -> //LD A,A
                    load8Bit.ld("A", "A");
            case 0x80 -> //ADD A,B
                    alu.add("B");
            case 0x81 -> //ADD A,C
                    alu.add("C");
            case 0x82 -> //ADD A,D
                    alu.add("D");
            case 0x83 -> //ADD A,E
                    alu.add("E");
            case 0x84 -> //ADD A, H
                    alu.add("H");
            case 0x85 -> //ADD A,L
                    alu.add("L");
            case 0x86 -> //ADD A,(HL)
                    alu.addSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0x87 -> //ADD A,A
                    alu.add("A");
            case 0x88 -> //ADC A,B
                    alu.adc("B");
            case 0x89 -> //ADC A,C
                    alu.adc("C");
            case 0x8A -> //ADC A,D
                    alu.adc("D");
            case 0x8B -> //ADC A,E
                    alu.adc("E");
            case 0x8C -> //ADC A,H
                    alu.adc("H");
            case 0x8D -> //ADC A,L
                    alu.adc("L");
            case 0x8E -> //ADC A,(HL)
                    alu.adcSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0x8F -> //ADC A,A
                    alu.adc("A");
            case 0x90 -> //SUB A,B
                    alu.sub("B");
            case 0x91 -> //SUB A,C
                    alu.sub("C");
            case 0x92 -> //SUB A,D
                    alu.sub("D");
            case 0x93 -> //SUB A,E
                    alu.sub("E");
            case 0x94 -> //SUB A,H
                    alu.sub("H");
            case 0x95 -> //SUB A,L
                    alu.sub("L");
            case 0x96 -> //SUB A, (HL)
                    alu.subSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0x97 -> //SUB A,A
                    alu.sub("A");
            case 0x98 -> //SBC A,B
                    alu.sbc("B");
            case 0x99 -> //SBC A,C
                    alu.sbc("C");
            case 0x9A -> //SBC A,D
                    alu.sbc("D");
            case 0x9B -> //SBC A,E
                    alu.sbc("E");
            case 0x9C -> //SBC A,H
                    alu.sbc("H");
            case 0x9D -> //SBC A,L
                    alu.sbc("L");
            case 0x9E -> //SBC A, (HL)
                    alu.sbcSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0x9F -> //SBC A,A
                    alu.sbc("A");
            case 0xA0 -> //AND A,B
                    alu.and("B");
            case 0xA1 -> //AND A,C
                    alu.and("C");
            case 0xA2 -> //AND A,D
                    alu.and("D");
            case 0xA3 -> //AND A,E
                    alu.and("E");
            case 0xA4 -> //AND A,H
                    alu.and("H");
            case 0xA5 -> //AND A,L
                    alu.and("L");
            case 0xA6 -> //AND A,(HL)
                    alu.andSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0xA7 -> //AND A,A
                    alu.and("A");
            case 0xA8 -> //XOR A,B
                    alu.xor("B");
            case 0xA9 -> //XOR A,C
                    alu.xor("C");
            case 0xAA -> //XOR A,D
                    alu.xor("D");
            case 0xAB -> //XOR A,E
                    alu.xor("E");
            case 0xAC -> //XOR A,H
                    alu.xor("H");
            case 0xAD -> //XOR A,L
                    alu.xor("L");
            case 0xAE -> //XOR A,(HL)
                    alu.xorSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0xAF -> //XOR A,A
                    alu.xor("A");
            case 0xB0 -> //OR A,B
                    alu.or("B");
            case 0xB1 -> //OR A,C
                    alu.or("C");
            case 0xB2 -> //OR A,D
                    alu.or("D");
            case 0xB3 -> //OR A,E
                    alu.or("E");
            case 0xB4 -> //OR A,H
                    alu.or("H");
            case 0xB5 -> //OR A,L
                    alu.or("L");
            case 0xB6 -> //OR A,(HL)
                    alu.orSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0xB7 -> //OR A,A
                    alu.or("A");
            case 0xB8 -> //CP A,B
                    alu.cp("B");
            case 0xB9 -> //CP A,C
                    alu.cp("C");
            case 0xBA -> //CP A,D
                    alu.cp("D");
            case 0xBB -> //CP A,E
                    alu.cp("E");
            case 0xBC -> //CP A,H
                    alu.cp("H");
            case 0xBD -> //CP A,L
                    alu.cp("L");
            case 0xBE -> //CP A,(HL)
                    alu.cpSpecial((Integer) bus.getFromCPU(Bus.GET_HL, null), true);
            case 0xBF -> //CP A,A
                    alu.cp("A");
            case 0xC0 -> //RET NZ
                    jump.retCond("NZ");
            case 0xC1 -> //POP BC
                    load16Bit.pop(1);
            case 0xC2 -> //JP NZ,u16
                    jump.jpCond("NZ");
            case 0xC3 -> //JP u16
                    jump.jp();
            case 0xC4 -> //CALL NZ, nn
                    jump.callCond("NZ");
            case 0xC5 -> //PUSH BC
                    load16Bit.push(1);
            case 0xC6 -> //ADD A,#
                    alu.addSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xC7 -> //RST 00H
                    jump.rst(0);
            case 0xC8 -> //RET Z
                    jump.retCond("Z");
            case 0xC9 -> //RET
                    jump.ret();
            case 0xCA -> //JP Z,u16
                    jump.jpCond("Z");
            case 0xCB -> {
                CB = true;
                bus.executeFromCPU(Bus.INCR_PC, new String[]{"1"});
                decode(bus.getValue((Integer) bus.getFromCPU(Bus.GET_PC, null)));
            }
            case 0xCC -> //CALL Z,nn
                    jump.callCond("Z");
            case 0xCD -> //CALL u16
                    jump.call();
            case 0xCE -> //ADC A,#
                    alu.adcSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xCF -> //RST 08H
                    jump.rst(1);
            case 0xD0 -> //RET NC
                    jump.retCond("NC");
            case 0xD1 -> //POP DE
                    load16Bit.pop(2);
            case 0xD2 -> //JP NC,u16
                    jump.jpCond("NC");
            case 0xD4 -> //CALL NC,nn
                    jump.callCond("NC");
            case 0xD5 -> //PUSH DE
                    load16Bit.push(2);
            case 0xD6 -> //SUB A, #
                    alu.subSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1,false);
            case 0xD7 -> //RST 10H
                    jump.rst(2);
            case 0xD8 -> //RET C
                    jump.retCond("C");
            case 0xD9 -> //RETI
                    jump.reti();
            case 0xDA -> //JP C,u16
                    jump.jpCond("C");
            case 0xDC -> //CALL C,nn
                    jump.callCond("C");
            case 0xDE -> //SBC A,#
                    alu.sbcSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xDF -> //RST 18H
                    jump.rst(3);
            case 0xE0 -> //LD (FF00+u8),A
                    load8Bit.ldh(true);
            case 0xE1 -> //POP (HL)
                    load16Bit.pop(3);
            case 0xE2 -> //LD (C), A
                    load8Bit.ldAC(true);
            case 0xE5 -> //PUSH HL
                    load16Bit.push(3);
            case 0xE6 -> //AND #
                    alu.andSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xE7 -> //RST 20H
                    jump.rst(4);
            case 0xE8 -> //ADD SP,n
                    alu.addSP((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1);
            case 0xE9 -> //JP (HL)
                    jump.jpHL();
            case 0xEA -> //LD (nn),A
                    load8Bit.ldNN();
            case 0xEE -> //XOR #
                    alu.xorSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xEF -> //RST 28H
                    jump.rst(5);
            case 0xF0 -> //LD A,(FF00+u8)
                    load8Bit.ldh(false);
            case 0xF1 -> //POP AF
                    load16Bit.pop(0);
            case 0xF2 -> //LD A,(C)
                    load8Bit.ldAC(false);
            case 0xF3 -> //DI
                    control.di();
            case 0xF5 -> //PUSH AF
                    load16Bit.push(0);
            case 0xF6 -> //OR #
                    alu.orSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xF7 -> //RST 30H
                    jump.rst(6);
            case 0xF8 -> //LDHL SP,n
                    load16Bit.LDHL();
            case 0xF9 -> //LD SP,HL
                    load16Bit.ldSPHL();
            case 0xFA -> //LD A,(nn)
                    load8Bit.ldNNIntoA();
            case 0xFB -> //EI
                    control.ei();
            case 0xFE -> //CP A,u8
                    alu.cpSpecial((Integer) bus.getFromCPU(Bus.GET_PC, null) + 1, false);
            case 0xFF -> //RST 38H
                    jump.rst(7);
            default -> {
                System.out.println("No OPCode or Lacks Implementation");
                System.exit(0);
            }
        }
    }
    private void handleCBOps(int operationCode) {
        switch (operationCode) {
            case 0x00 -> //RLC B
                    rotateShift.rlc("B");
            case 0x01 -> //RLC C
                    rotateShift.rlc("C");
            case 0x02 -> //RLC D
                    rotateShift.rlc("D");
            case 0x03 -> //RLC E
                    rotateShift.rlc("E");
            case 0x04 -> //RLC H
                    rotateShift.rlc("H");
            case 0x05 -> //RLC L
                    rotateShift.rlc("L");
            case 0x06 -> //RLC HL
                    rotateShift.rlcHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x07 -> //RLC A
                    rotateShift.rlc("A");
            case 0x08 -> //RRC B
                    rotateShift.rrc("B");
            case 0x09 -> //RRC C
                    rotateShift.rrc("C");
            case 0x0A -> //RRC D
                    rotateShift.rrc("D");
            case 0x0B -> //RRC E
                    rotateShift.rrc("E");
            case 0x0C -> //RRC H
                    rotateShift.rrc("H");
            case 0x0D -> //RRC L
                    rotateShift.rrc("L");
            case 0x0E -> //RRC (HL)
                    rotateShift.rrcHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x0F -> //RRC A
                    rotateShift.rrc("A");
            case 0x10 -> //RL B
                    rotateShift.rl("B");
            case 0x11 -> //RL C
                    rotateShift.rl("C");
            case 0x12 -> //RL D
                    rotateShift.rl("D");
            case 0x13 -> //RL E
                    rotateShift.rl("E");
            case 0x14 -> //RL H
                    rotateShift.rl("H");
            case 0x15 -> //RL L
                    rotateShift.rl("L");
            case 0x16 -> //RL (HL)
                    rotateShift.rlHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x17 -> //RL A
                    rotateShift.rl("A");
            case 0x18 -> //RR B
                    rotateShift.rr("B");
            case 0x19 -> //RR C
                    rotateShift.rr("C");
            case 0x1A -> //RR D
                    rotateShift.rr("D");
            case 0x1B -> //RR E
                    rotateShift.rr("E");
            case 0x1C -> //RR H
                    rotateShift.rr("H");
            case 0x1D -> //RR L
                    rotateShift.rr("L");
            case 0x1E -> //RR (HL)
                    rotateShift.rrHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x1F -> //RR A
                    rotateShift.rr("A");
            case 0x20 -> //SLA B
                    rotateShift.sla("B");
            case 0x21 -> //SLA C
                    rotateShift.sla("C");
            case 0x22 -> //SLA D
                    rotateShift.sla("D");
            case 0x23 -> //SLA E
                    rotateShift.sla("E");
            case 0x24 -> //SLA H
                    rotateShift.sla("H");
            case 0x25 -> //SLA L
                    rotateShift.sla("L");
            case 0x26 -> //SLA (HL)
                    rotateShift.slaHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x27 -> //SLA A
                    rotateShift.sla("A");
            case 0x28 -> //SRA B
                    rotateShift.sra("B");
            case 0x29 -> //SRA C
                    rotateShift.sra("C");
            case 0x2A -> //SRA D
                    rotateShift.sra("D");
            case 0x2B -> //SRA E
                    rotateShift.sra("E");
            case 0x2C -> //SRA H
                    rotateShift.sra("H");
            case 0x2D -> //SRA L
                    rotateShift.sra("L");
            case 0x2E -> //SRA (HL)
                    rotateShift.sraHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x2F -> //SRA A
                    rotateShift.sra("A");
            case 0x30 -> //SWAP B
                    rotateShift.swap("B");
            case 0x31 -> //SWAP C
                    rotateShift.swap("C");
            case 0x32 -> //SWAP D
                    rotateShift.swap("D");
            case 0x33 -> //SWAP E
                    rotateShift.swap("E");
            case 0x34 -> //SWAP H
                    rotateShift.swap("H");
            case 0x35 -> //SWAP L
                    rotateShift.swap("L");
            case 0x36 -> //SWAP (HL)
                    rotateShift.swapHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x37 -> //SWAP A
                    rotateShift.swap("A");
            case 0x38 -> //SRL B
                    rotateShift.srl("B");
            case 0x39 -> //SRL C
                    rotateShift.srl("C");
            case 0x3A -> //SRL D
                    rotateShift.srl("D");
            case 0x3B -> //SRL E
                    rotateShift.srl("E");
            case 0x3C -> //SRL H
                    rotateShift.srl("H");
            case 0x3D -> //SRL L
                    rotateShift.srl("L");
            case 0x3E -> //SRL (HL)
                    rotateShift.srlHL((Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x3F -> //SRL A
                    rotateShift.srl("A");
            case 0x40 -> //BIT 0,B
                    singleBit.bit(0, "B");
            case 0x41 -> //BIT 0,C
                    singleBit.bit(0, "C");
            case 0x42 -> //BIT 0,D
                    singleBit.bit(0, "D");
            case 0x43 -> //BIT 0,E
                    singleBit.bit(0, "E");
            case 0x44 -> //BIT 0,H
                    singleBit.bit(0, "H");
            case 0x45 -> //BIT 0,L
                    singleBit.bit(0, "L");
            case 0x46 -> //BIT 0,(HL)
                    singleBit.bitHL(0, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x47 -> //BIT 0,A
                    singleBit.bit(0, "A");
            case 0x48 -> //BIT 1,B
                    singleBit.bit(1, "B");
            case 0x49 -> //BIT 1,C
                    singleBit.bit(1, "C");
            case 0x4A -> //BIT 1,D
                    singleBit.bit(1, "D");
            case 0x4B -> //BIT 1,E
                    singleBit.bit(1, "E");
            case 0x4C -> //BIT 1,H
                    singleBit.bit(1, "H");
            case 0x4D -> //BIT 1,L
                    singleBit.bit(1, "L");
            case 0x4E -> //BIT 1,(HL)
                    singleBit.bitHL(1, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x4F -> //BIT 1,A
                    singleBit.bit(1, "A");
            case 0x50 -> //BIT 2,B
                    singleBit.bit(2, "B");
            case 0x51 -> //BIT 2,C
                    singleBit.bit(2, "C");
            case 0x52 -> //BIT 2,D
                    singleBit.bit(2, "D");
            case 0x53 -> //BIT 2,E
                    singleBit.bit(2, "E");
            case 0x54 -> //BIT 2,H
                    singleBit.bit(2, "H");
            case 0x55 -> //BIT 2,L
                    singleBit.bit(2, "L");
            case 0x56 -> //BIT 2,(HL)
                    singleBit.bitHL(2, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x57 -> //BIT 2,A
                    singleBit.bit(2, "A");
            case 0x58 -> //BIT 3,B
                    singleBit.bit(3, "B");
            case 0x59 -> //BIT 3,C
                    singleBit.bit(3, "C");
            case 0x5A -> //BIT 3,D
                    singleBit.bit(3, "D");
            case 0x5B -> //BIT 3,E
                    singleBit.bit(3, "E");
            case 0x5C -> //BIT 3,H
                    singleBit.bit(3, "H");
            case 0x5D -> //BIT 3,L
                    singleBit.bit(3, "L");
            case 0x5E -> //BIT 3,(HL)
                    singleBit.bitHL(3, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x5F -> //BIT 3,A
                    singleBit.bit(3, "A");
            case 0x60 -> //BIT 4,B
                    singleBit.bit(4, "B");
            case 0x61 -> //BIT 4,C
                    singleBit.bit(4, "C");
            case 0x62 -> //BIT 4,D
                    singleBit.bit(4, "D");
            case 0x63 -> //BIT 4,E
                    singleBit.bit(4, "E");
            case 0x64 -> //BIT 4,H
                    singleBit.bit(4, "H");
            case 0x65 -> //BIT 4,L
                    singleBit.bit(4, "L");
            case 0x66 -> //BIT 4,(HL)
                    singleBit.bitHL(4, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x67 -> //BIT 4,A
                    singleBit.bit(4, "A");
            case 0x68 -> //BIT 5,B
                    singleBit.bit(5, "B");
            case 0x69 -> //BIT 5,C
                    singleBit.bit(5, "C");
            case 0x6A -> //BIT 5,D
                    singleBit.bit(5, "D");
            case 0x6B -> //BIT 5,E
                    singleBit.bit(5, "E");
            case 0x6C -> //BIT 5,H
                    singleBit.bit(5, "H");
            case 0x6D -> //BIT 5,L
                    singleBit.bit(5, "L");
            case 0x6E -> //BIT 5,(HL)
                    singleBit.bitHL(5, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x6F -> //BIT 5,A
                    singleBit.bit(5, "A");
            case 0x70 -> //BIT 6,B
                    singleBit.bit(6, "B");
            case 0x71 -> //BIT 6,C
                    singleBit.bit(6, "C");
            case 0x72 -> //BIT 6,D
                    singleBit.bit(6, "D");
            case 0x73 -> //BIT 6,E
                    singleBit.bit(6, "E");
            case 0x74 -> //BIT 6,H
                    singleBit.bit(6, "H");
            case 0x75 -> //BIT 6,L
                    singleBit.bit(6, "L");
            case 0x76 -> //BIT 6,(HL)
                    singleBit.bitHL(6, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x77 -> //BIT 6,A
                    singleBit.bit(6, "A");
            case 0x78 -> //BIT 7,B
                    singleBit.bit(7, "B");
            case 0x79 -> //BIT 7,C
                    singleBit.bit(7, "C");
            case 0x7A -> //BIT 7,D
                    singleBit.bit(7, "D");
            case 0x7B -> //BIT 7,E
                    singleBit.bit(7, "E");
            case 0x7C -> //BIT 7,H
                    singleBit.bit(7, "H");
            case 0x7D -> //BIT 7,L
                    singleBit.bit(7, "L");
            case 0x7E -> //BIT 7, (HL)
                    singleBit.bitHL(7, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x7F -> //BIT 7,A
                    singleBit.bit(7, "A");
            case 0x80 -> //RES 0,B
                    singleBit.res(0, "B");
            case 0x81 -> //RES 0,C
                    singleBit.res(0, "C");
            case 0x82 -> //RES 0,D
                    singleBit.res(0, "D");
            case 0x83 -> //RES 0,E
                    singleBit.res(0, "E");
            case 0x84 -> //RES 0,H
                    singleBit.res(0, "H");
            case 0x85 -> //RES 0,L
                    singleBit.res(0, "L");
            case 0x86 -> //RES 0,(HL)
                    singleBit.resHL(0, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x87 -> //RES 0,A
                    singleBit.res(0, "A");
            case 0x88 -> //RES 1,B
                    singleBit.res(1, "B");
            case 0x89 -> //RES 1,C
                    singleBit.res(1, "C");
            case 0x8A -> //RES 1,D
                    singleBit.res(1, "D");
            case 0x8B -> //RES 1,E
                    singleBit.res(1, "E");
            case 0x8C -> //RES 1,H
                    singleBit.res(1, "H");
            case 0x8D -> //RES 1,L
                    singleBit.res(1, "L");
            case 0x8E -> //RES 1,(HL)
                    singleBit.resHL(1, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x8F -> //RES 1,A
                    singleBit.res(1, "A");
            case 0x90 -> //RES 2,B
                    singleBit.res(2, "B");
            case 0x91 -> //RES 2,C
                    singleBit.res(2, "C");
            case 0x92 -> //RES 2,D
                    singleBit.res(2, "D");
            case 0x93 -> //RES 2,E
                    singleBit.res(2, "E");
            case 0x94 -> //RES 2,H
                    singleBit.res(2, "H");
            case 0x95 -> //RES 2,L
                    singleBit.res(2, "L");
            case 0x96 -> //RES 2,(HL)
                    singleBit.resHL(2, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x97 -> //RES 2,A
                    singleBit.res(2, "A");
            case 0x98 -> //RES 3,B
                    singleBit.res(3, "B");
            case 0x99 -> //RES 3,C
                    singleBit.res(3, "C");
            case 0x9A -> //RES 3,D
                    singleBit.res(3, "D");
            case 0x9B -> //RES 3,E
                    singleBit.res(3, "E");
            case 0x9C -> //RES 3,H
                    singleBit.res(3, "H");
            case 0x9D -> //RES 3,L
                    singleBit.res(3, "L");
            case 0x9E -> //RES 3,(HL)
                    singleBit.resHL(3, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0x9F -> //RES 3,A
                    singleBit.res(3, "A");
            case 0xA0 -> //RES 4,B
                    singleBit.res(4, "B");
            case 0xA1 -> //RES 4,C
                    singleBit.res(4, "C");
            case 0xA2 -> //RES 4,D
                    singleBit.res(4, "D");
            case 0xA3 -> //RES 4,E
                    singleBit.res(4, "E");
            case 0xA4 -> //RES 4,H
                    singleBit.res(4, "H");
            case 0xA5 -> //RES 4,L
                    singleBit.res(4, "L");
            case 0xA6 -> //RES 4,(HL)
                    singleBit.resHL(4, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xA7 -> //RES 4,A
                    singleBit.res(4, "A");
            case 0xA8 -> //RES 5,B
                    singleBit.res(5, "B");
            case 0xA9 -> //RES 5,C
                    singleBit.res(5, "C");
            case 0xAA -> //RES 5,D
                    singleBit.res(5, "D");
            case 0xAB -> //RES 5,E
                    singleBit.res(5, "E");
            case 0xAC -> //RES 5,H
                    singleBit.res(5, "H");
            case 0xAD -> //RES 5,L
                    singleBit.res(5, "L");
            case 0xAE -> //RES 5,(HL)
                    singleBit.resHL(5, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xAF -> //RES 5,A
                    singleBit.res(5, "A");
            case 0xB0 -> //RES 6,B
                    singleBit.res(6, "B");
            case 0xB1 -> //RES 6,C
                    singleBit.res(6, "C");
            case 0xB2 -> //RES 6,D
                    singleBit.res(6, "D");
            case 0xB3 -> //RES 6,E
                    singleBit.res(6, "E");
            case 0xB4 -> //RES 6,H
                    singleBit.res(6, "H");
            case 0xB5 -> //RES 6,L
                    singleBit.res(6, "L");
            case 0xB6 -> //RES 6,(HL)
                    singleBit.resHL(6, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xB7 -> //RES 6,A
                    singleBit.res(6, "A");
            case 0xB8 -> //RES 7,B
                    singleBit.res(7, "B");
            case 0xB9 -> //RES 7,C
                    singleBit.res(7, "C");
            case 0xBA -> //RES 7,D
                    singleBit.res(7, "D");
            case 0xBB -> //RES 7,E
                    singleBit.res(7, "E");
            case 0xBC -> //RES 7,H
                    singleBit.res(7, "H");
            case 0xBD -> //RES 7,L
                    singleBit.res(7, "L");
            case 0xBE -> //RES 7,(HL)
                    singleBit.resHL(7, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xBF -> //RES 7,A
                    singleBit.res(7, "A");
            case 0xC0 -> //SET 0,B
                    singleBit.set(0, "B");
            case 0xC1 -> //SET 0,C
                    singleBit.set(0, "C");
            case 0xC2 -> //SET 0,D
                    singleBit.set(0, "D");
            case 0xC3 -> //SET 0,E
                    singleBit.set(0, "E");
            case 0xC4 -> //SET 0,H
                    singleBit.set(0, "H");
            case 0xC5 -> //SET 0,L
                    singleBit.set(0, "L");
            case 0xC6 -> //SET 0,(HL)
                    singleBit.setHL(0, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xC7 -> //SET 0,A
                    singleBit.set(0, "A");
            case 0xC8 -> //SET 1,B
                    singleBit.set(1, "B");
            case 0xC9 -> //SET 1,C
                    singleBit.set(1, "C");
            case 0xCA -> //SET 1,D
                    singleBit.set(1, "D");
            case 0xCB -> //SET 1,E
                    singleBit.set(1, "E");
            case 0xCC -> //SET 1,H
                    singleBit.set(1, "H");
            case 0xCD -> //SET 1,L
                    singleBit.set(1, "L");
            case 0xCE -> //SET 1,(HL)
                    singleBit.setHL(1, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xCF -> //SET 1,A
                    singleBit.set(1, "A");
            case 0xD0 -> //SET 2,B
                    singleBit.set(2, "B");
            case 0xD1 -> //SET 2,C
                    singleBit.set(2, "C");
            case 0xD2 -> //SET 2,D
                    singleBit.set(2, "D");
            case 0xD3 -> //SET 2,E
                    singleBit.set(2, "E");
            case 0xD4 -> //SET 2,H
                    singleBit.set(2, "H");
            case 0xD5 -> //SET 2,L
                    singleBit.set(2, "L");
            case 0xD6 -> //SET 2,(HL)
                    singleBit.setHL(2, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xD7 -> //SET 2,A
                    singleBit.set(2, "A");
            case 0xD8 -> //SET 3,B
                    singleBit.set(3, "B");
            case 0xD9 -> //SET 3,C
                    singleBit.set(3, "C");
            case 0xDA -> //SET 3,D
                    singleBit.set(3, "D");
            case 0xDB -> //SET 3,E
                    singleBit.set(3, "E");
            case 0xDC -> //SET 3,H
                    singleBit.set(3, "H");
            case 0xDD -> //SET 3,L
                    singleBit.set(3, "L");
            case 0xDE -> //SET 3,(HL)
                    singleBit.setHL(3, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xDF -> //SET 3,A
                    singleBit.set(3, "A");
            case 0xE0 -> //SET 4,B
                    singleBit.set(4, "B");
            case 0xE1 -> //SET 4,C
                    singleBit.set(4, "C");
            case 0xE2 -> //SET 4,D
                    singleBit.set(4, "D");
            case 0xE3 -> //SET 4,E
                    singleBit.set(4, "E");
            case 0xE4 -> //SET 4,H
                    singleBit.set(4, "H");
            case 0xE5 -> //SET 4,L
                    singleBit.set(4, "L");
            case 0xE6 -> //SET 4,(HL)
                    singleBit.setHL(4, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xE7 -> //SET 4,A
                    singleBit.set(4, "A");
            case 0xE8 -> //SET 5,B
                    singleBit.set(5, "B");
            case 0xE9 -> //SET 5,C
                    singleBit.set(5, "C");
            case 0xEA -> //SET 5,D
                    singleBit.set(5, "D");
            case 0xEB -> //SET 5,E
                    singleBit.set(5, "E");
            case 0xEC -> //SET 5,H
                    singleBit.set(5, "H");
            case 0xED -> //SET 5,L
                    singleBit.set(5, "L");
            case 0xEE -> //SET 5,(HL)
                    singleBit.setHL(5, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xEF -> //SET 5,A
                    singleBit.set(5, "A");
            case 0xF0 -> //SET 6,B
                    singleBit.set(6, "B");
            case 0xF1 -> //SET 6,C
                    singleBit.set(6, "C");
            case 0xF2 -> //SET 6,D
                    singleBit.set(6, "D");
            case 0xF3 -> //SET 6,E
                    singleBit.set(6, "E");
            case 0xF4 -> //SET 6,H
                    singleBit.set(6, "H");
            case 0xF5 -> //SET 6,L
                    singleBit.set(6, "L");
            case 0xF6 -> //SET 6,(HL)
                    singleBit.setHL(6, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xF7 -> //SET 6,A
                    singleBit.set(6, "A");
            case 0xF8 -> //SET 7,B
                    singleBit.set(7, "B");
            case 0xF9 -> //SET 7,C
                    singleBit.set(7, "C");
            case 0xFA -> //SET 7,D
                    singleBit.set(7, "D");
            case 0xFB -> //SET 7,E
                    singleBit.set(7, "E");
            case 0xFC -> //SET 7,H
                    singleBit.set(7, "H");
            case 0xFD -> //SET 7,L
                    singleBit.set(7, "L");
            case 0xFE -> //SET 7,(HL)
                    singleBit.setHL(7, (Integer) bus.getFromCPU(Bus.GET_HL, null));
            case 0xFF -> //SET 7,A
                    singleBit.set(7, "A");
            default -> {
                System.out.println("No OPCode or Lacks Implementation");
                System.exit(0);
            }
        }
    }
}
