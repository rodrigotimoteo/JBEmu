package io.github.memory;

/**
 * Class responsible for managing everything interacting directly with the
 * emulator memory space, keeps all other modules and manages addresses
 *
 * @author rodrigotimoteo
 */

public class MemoryManager implements MemoryManipulation {

    /**
     * Reference to instances bus
     */
    private Bus bus;

    /**
     *
     */
    private final MemoryModule rom;
    //        private final MemoryModule vram;
//        private final MemoryModule eram;
//        private final MemoryModule wram;
    private final MemoryModule oam;
    private final MemoryModule bottomRegisters;

    /** Constructor Method
     *
     * <p></p>
     *
     * @param bus
     * @param rom
     */
    public MemoryManager(Bus bus, MemoryModule rom) {
        this.bus = bus;
        this.rom = rom;




//            wram = new MemoryModule()
        oam = new MemoryModule(0xA0, ReservedAddresses.OAM_START.getAddress());
        bottomRegisters = new MemoryModule(0x100, ReservedAddresses.IO_START.getAddress());
    }

    /**
     * Changes value of specific word based on its address
     *
     * @param address where to change the value
     * @param value to assign to the word
     */
    public void setValue(int address, int value) {

    }

    /**
     * Gets the value of specific word based on its address
     *
     * @param address where to retrieve the value
     * @return value stored in specific address
     */
    public int getValue(int address) {
        return 0;
    }

    /**
     * Gets the specific word based on its address
     *
     * @param address where to retrieve the value
     * @return Word stored in specific address
     */
    public Word getWord(int address) {
        return null;
    }

    /**
     * Responsible for initializing the memory with the default values assigned
     * in the boot rom
     */
    private void init() {
        getWord(ReservedAddresses.NR10.getAddress()).setValue(0x80);
        getWord(ReservedAddresses.NR11.getAddress()).setValue(0xBF);
        getWord(ReservedAddresses.NR12.getAddress()).setValue(0xF3);
        getWord(ReservedAddresses.NR14.getAddress()).setValue(0xBF);
        getWord(ReservedAddresses.NR21.getAddress()).setValue(0x3F);
        getWord(ReservedAddresses.NR24.getAddress()).setValue(0xBF);
        getWord(ReservedAddresses.NR30.getAddress()).setValue(0x7F);
        getWord(ReservedAddresses.NR31.getAddress()).setValue(0xFF);
        getWord(ReservedAddresses.NR32.getAddress()).setValue(0x9F);
        getWord(ReservedAddresses.NR34.getAddress()).setValue(0xBF);
        getWord(ReservedAddresses.NR41.getAddress()).setValue(0xFF);
        getWord(ReservedAddresses.NR44.getAddress()).setValue(0xBF);
        getWord(ReservedAddresses.NR50.getAddress()).setValue(0x77);
        getWord(ReservedAddresses.NR51.getAddress()).setValue(0xF3);
        getWord(ReservedAddresses.NR52.getAddress()).setValue(0xF1);
        getWord(ReservedAddresses.LCDC.getAddress()).setValue(0x91);
        getWord(ReservedAddresses.STAT.getAddress()).setValue(0x80);
        getWord(ReservedAddresses.BGP.getAddress() ).setValue(0xFC);
        getWord(ReservedAddresses.OBP0.getAddress()).setValue(0xFF);
        getWord(ReservedAddresses.OBP1.getAddress()).setValue(0xFF);

        //Debug Purposes LY
        //getByte(ReservedAddresses.LY.getAddress()).setValue(0x90);
    }


}