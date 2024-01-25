package io.github.memory;

import io.github.cpu.CPU;
import io.github.ppu.PPU;

/**
 * Responsible for interactions between the various system components and
 * storing the main memory
 *
 * @author rodrigotimoteo
 */


public class Bus {

    /**
     * Stores CPU Getter codes
     */
    public static final int GET_FLAGS       = 0;
    public static final int GET_REGISTER    = 1;
    public static final int GET_AF          = 2;
    public static final int GET_BC          = 3;
    public static final int GET_DE          = 4;
    public static final int GET_HL          = 5;
    public static final int GET_PC          = 6;
    public static final int GET_SP          = 7;

    /**
     * Stores CPU Execution codes
     */
    public static final int TICK_TIMERS     = 0;
    public static final int SET_REGISTER    = 1;
    public static final int INCR_PC         = 2;
    public static final int SET_PC          = 3;
    public static final int INCR_SP         = 4;
    public static final int SET_SP          = 5;
    public static final int SET_AF          = 6;
    public static final int SET_BC          = 7;
    public static final int SET_DE          = 8;
    public static final int SET_HL          = 9;
    public static final int DISABLE_INT     = 10;
    public static final int ENABLE_INT      = 11;
    public static final int HALT            = 12;
    public static final int STOP            = 13;


    /**
     * Stores a reference to the CPU
     */
    private CPU cpu;

    /**
     * Stores a reference to the PPU
     */
    private PPU ppu;

    /**
     * Stores a reference to the Memory Manager
     */
    private final MemoryManager memory;

    /**
     * Stores whether it should run in CGB mode
     */
    private final boolean cgb;

    /**
     * Responsible for initializing the Bus creating the Memory Space in this
     * instance
     *
     * @param cgb whether to use CGB mode (true if yes false otherwise)
     * @param rom memory module corresponding for specific Memory Bank Controller
     *            with the rom already written to it
     */
    public Bus(boolean cgb, MemoryModule rom) {
        this.cgb = cgb;

        memory = new MemoryManager(this, rom);
    }

    /**
     * Setter to assign the bus cpu access
     *
     * @param cpu reference for this instance's cpu
     */
    public void setCpu(CPU cpu) {
        if(this.cpu == null) this.cpu = cpu;
    }

    /**
     * Setter to assign the bus ppu access
     *
     * @param ppu reference for this instance's ppu
     */
    public void setPpu(PPU ppu) {
        if(this.ppu == null) this.ppu = ppu;
    }

    /**
     * Returns whether to run or not in CGB mode
     *
     * @return boolean (true if CGB mode false otherwise)
     */
    public boolean isCgb() {
        return cgb;
    }

    //Memory Interaction Methods

    /**
     * Changes value of specific word based on its address
     *
     * @param address where to change the value
     * @param value to assign to the word
     */
    public void setValue(int address, int value) {
        memory.setValue(address, value);
    }

    /**
     * Gets the value of specific word based on its address
     *
     * @param address where to change the value
     * @return value stored in specific address
     */
    public int getValue(int address) {
        return memory.getValue(address);
    }

    /**
     * Gets the specific word based on its address
     *
     * @param address where to retrieve the value
     * @return Word stored in specific address
     */
    public Word getWord(int address) {
        return memory.getWord(address);
    }

    /**
     * Dumps all the memory as print statement
     */
    public void requestMemoryDump() {
        System.out.println(memory);
    }

    //CPU Interaction Methods

    /**
     * Follows a structured table in order to retrieve information from the cpu
     * based on codes
     *
     * @param code information identifier
     * @param parameters additional information that might be required to execute
     *                   some methods
     * @return asked information
     */
    public Object getFromCPU(int code, String[] parameters) {
        return switch (code) {
            case GET_FLAGS      -> cpu.getRegisters().getFlags();
            case GET_REGISTER   -> cpu.getRegisters().getRegister(parameters[0]);
            case GET_AF         -> cpu.getRegisters().getAF();
            case GET_BC         -> cpu.getRegisters().getBC();
            case GET_DE         -> cpu.getRegisters().getDE();
            case GET_HL         -> cpu.getRegisters().getHL();
            case GET_PC         -> cpu.getRegisters().getProgramCounter();
            case GET_SP         -> cpu.getRegisters().getStackPointer();
            default ->
                    throw new IllegalStateException("Unexpected value: " + code);
        };
    }

    /**
     * Follows a structured table in order to execute operations inside the cpu
     * based on codes
     *
     * @param code information identifier
     * @param parameters additional information that might be required to execute
     *                   some methods
     */
    public void executeFromCPU(int code, String[] parameters) {
        switch (code) {
            case TICK_TIMERS    -> cpu.getTimers().tick();
            case SET_REGISTER   -> cpu.getRegisters()
                    .setRegister(parameters[0], Integer.parseInt(parameters[1]));
            case INCR_PC        -> cpu.getRegisters()
                    .incrementProgramCounter(Integer.parseInt(parameters[0]));
            case SET_PC         -> cpu.getRegisters()
                    .setProgramCounter(Integer.parseInt(parameters[0]));
            case INCR_SP        -> cpu.getRegisters()
                    .incrementStackPointer(Integer.parseInt(parameters[0]));
            case SET_SP         -> cpu.getRegisters()
                    .setStackPointer(Integer.parseInt(parameters[0]));
            case SET_AF         -> cpu.getRegisters()
                    .setAF(Integer.parseInt(parameters[0]));
            case SET_BC         -> cpu.getRegisters()
                    .setBC(Integer.parseInt(parameters[0]));
            case SET_DE         -> cpu.getRegisters()
                    .setDE(Integer.parseInt(parameters[0]));
            case SET_HL         -> cpu.getRegisters()
                    .setHL(Integer.parseInt(parameters[0]));
            case DISABLE_INT    -> cpu.interruptChange(false);
            case ENABLE_INT     -> cpu.interruptChange(true);
            case HALT           -> cpu.setHalted(true);
            case STOP           -> cpu.setStopped(true);
        };
    }

    /**
     * Using the program counter uses the next two word to build a new address
     * usually called NN
     *
     * @return address calculated
     */
    public int calculateNN() {
        int programCounter = (Integer) getFromCPU(Bus.GET_PC, null);

        executeFromCPU(Bus.TICK_TIMERS, null);
        int lowerAddress = getValue(programCounter + 1);
        executeFromCPU(Bus.TICK_TIMERS, null);
        int upperAddress = getValue(programCounter + 2) << 8;

        return upperAddress + lowerAddress;
    }
}


