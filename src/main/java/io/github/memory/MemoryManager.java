package io.github.memory;

import java.util.HashMap;

/**
 * Class responsible for managing everything interacting directly with the
 * emulator memory space, keeps all other modules and manages addresses
 *
 *<p>0x0000 to 0x4000 - 16kb ROM Bank #0
 *   0x4000 to 0x8000 - 16kb switchable ROM Bank
 *   0x8000 to 0xA000 - 8kb Video RAM
 *   0xA000 to 0xC000 - 8kb switchable RAM Bank
 *   0xC000 to 0xE000 - 8kb Internal RAM
 *   0xE000 to 0xFE00 - Echo of 8kb Internal RAM
 *   0xFE00 to 0xFEA0 - Sprite Attrib Memory (OAM)
 *   0xFEA0 to 0xFF00 - Empty but unusable for I/O
 *   0xFF00 to 0xFF4C - I/O Ports
 *   0xFF4C to 0xFF80 - Empty but unusable for I/O
 *   0xFF80 to 0xFFFF - Internal RAM
 *   0xFFFF           - Interrupt Enable Register</p>
 *
 * @author rodrigotimoteo
 */

public class MemoryManager implements MemoryManipulation {

    /**
     * Stores a direct translation between the bit representing the ram in cartridge
     * and the number of ram banks that should be created
     */
    private final HashMap<Integer,Integer> ramBanksMap = new HashMap<>();
    {
        ramBanksMap.put(0, 0);
        ramBanksMap.put(1, 1);
        ramBanksMap.put(2, 1);
        ramBanksMap.put(3, 4);
        ramBanksMap.put(4, 16);
        ramBanksMap.put(5, 8);
    }

    /**
     * Reference to instances bus
     */
    private final Bus bus;

    /**
     * Reference to the ROM memory module
     */
    private final MemoryModule rom;

    /**
     * Reference to the VRAM memory module
     */
    private final MemoryModule vram;

    /**
     * Reference to the ERAM memory module
     */
    private final MemoryModule eram;

    /**
     * Reference to the WRAM memory module
     */
    private final MemoryModule wram;

    /**
     * Reference to the OAM memory module
     */
    private final MemoryModule oam;

    /**
     * Reference to the memory module that holds the last registers of the
     * memory space
     */
    private final MemoryModule bottomRegisters;

    /**
     * Stores whether it should run in CGB mode
     */
    private final boolean cgb;

    /** Constructor Method
     *
     * <p></p>
     *
     * @param bus reference to the bus in order to interact with other components
     *            as needed
     * @param rom contains a memory module where the rom is stored for later usage
     */
    public MemoryManager(Bus bus, MemoryModule rom) {
        this.bus = bus;

        cgb = bus.isCgb();

        this.rom = rom;

        if(cgb)
            vram = new MemoryModule(0x2000, 1, 0x8000, 2);
        else
            vram = new MemoryModule(0x2000, 0x8000);

        int numberOfRamBanks = getRamSize();
        if(numberOfRamBanks == 0)
            eram = null;
        else
            eram = new MemoryModule(0x2000, 1, 0xA000, numberOfRamBanks);

        if(cgb)
            wram = new MemoryModule(0x1000, 2, 0xC000, 8);
        else
            wram = new MemoryModule(0x1000, 2, 0xC000, 2);

        oam = new MemoryModule(0xA0, ReservedAddresses.OAM_START.getAddress());
        bottomRegisters = new MemoryModule(0x100, ReservedAddresses.IO_START.getAddress());

        init();
    }

    /**
     * Changes value of specific word based on its address
     *
     * @param address where to change the value
     * @param value to assign to the word
     */
    public void setValue(int address, int value) {
        if      (address < ReservedAddresses.SWITCH_ROM_END.getAddress())
            rom.setValue(address, value);
        else if (address < ReservedAddresses.VRAM_END.getAddress())
            vram.setValue(address, value);
        else if (address < ReservedAddresses.ERAM_END.getAddress() && eram != null)
            eram.setValue(address, value);
        else if (address < ReservedAddresses.WRAM_END.getAddress())
            wram.setValue(address, value);
        else if (address < ReservedAddresses.OAM_START.getAddress())
            return; //THIS SECTION IS ECHO RAM SHOULD NOT BE USED
        else if (address < ReservedAddresses.OAM_END.getAddress())
            oam.setValue(address, value);
        else if (address < ReservedAddresses.IO_START.getAddress())
            return; //THIS SECTION IS PROHIBITED
        else
            handleBottomRegisters(address, value);
    }

    /**
     * This method is responsible for handling the assignment of new values to
     * the bottom registers, according to all their quirks
     *
     * @param address where to change the value
     * @param value to assign to the word
     */
    private void handleBottomRegisters(int address, int value) {

    }

    /**
     * Gets the value of specific word based on its address
     *
     * @param address where to retrieve the value
     * @return value stored in specific address
     */
    public int getValue(int address) {
        if      (address < ReservedAddresses.SWITCH_ROM_END.getAddress())
            return rom.getValue(address);
        else if (address < ReservedAddresses.VRAM_END.getAddress())
            return vram.getValue(address);
        else if (address < ReservedAddresses.ERAM_END.getAddress() && eram != null)
            //MISSING RAM IMPLEMENTATION
            if(true)
                return eram.getValue(address);
            else
                return 0x00;
        else if (address < ReservedAddresses.WRAM_END.getAddress())
            return wram.getValue(address);
        else if (address < ReservedAddresses.OAM_START.getAddress())
            //THIS SECTION RETURNS WRAM UNTIL ITS END (ECHO RAM)
            return wram.getValue(address - 0x2000);
        else if (address < ReservedAddresses.OAM_END.getAddress())
            return oam.getValue(address);
        else if (address < ReservedAddresses.IO_START.getAddress())
            return 0; //THIS SECTION SHOULD NOT BE USED
        else
            return bottomRegisters.getValue(address);
    }

    /**
     * Gets the specific word based on its address
     *
     * @param address where to retrieve the value
     * @return Word stored in specific address
     */
    public Word getWord(int address) {
        if      (address < ReservedAddresses.SWITCH_ROM_END.getAddress())
            return rom.getWord(address);
        else if (address < ReservedAddresses.VRAM_END.getAddress())
            return vram.getWord(address);
        else if (address < ReservedAddresses.ERAM_END.getAddress() && eram != null)
            //MISSING RAM IMPLEMENTATION
            if(true)
                return eram.getWord(address);
            else
                return null;
        else if (address < ReservedAddresses.WRAM_END.getAddress())
            return wram.getWord(address);
        else if (address < ReservedAddresses.OAM_START.getAddress())
            //THIS SECTION RETURNS WRAM UNTIL ITS END (ECHO RAM)
            return wram.getWord(address - 0x2000);
        else if (address < ReservedAddresses.OAM_END.getAddress())
            return oam.getWord(address);
        else if (address < ReservedAddresses.IO_START.getAddress())
            return null; //THIS SECTION SHOULD NOT BE USED
        else
            return bottomRegisters.getWord(address);
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

    /**
     * Responsible for translating the hashmap into the number of ram banks the
     * rom needs
     *
     * @return number of ram banks
     */
    private int getRamSize() {
        int ramSize = rom.getValue(ReservedAddresses.ROM_SIZE.getAddress());

        return ramBanksMap.get(ramSize);
    }

    /**
     * Converts the full memory map into a readable string containing all the
     * memory address' content
     * Every line contains the content of 16 addresses until finished
     *
     * @return String with full memory dump
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("0 ");
        for(int i = 0; i < 0x10000; i++) {
            if(i % 16 == 0 && i != 0) {
                stringBuilder.append(" \n");
                stringBuilder.append(Integer.toHexString(i)).append(" ");
                stringBuilder.append(Integer.toHexString(getValue(i))).append(" ");
            } else
                stringBuilder.append(Integer.toHexString(getValue(i))).append(" ");
        }

        return stringBuilder.toString();
    }

}