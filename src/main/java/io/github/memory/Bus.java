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

}


