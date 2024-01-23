package io.github.memory.cartridge;

import io.github.memory.MemoryManager;
import io.github.memory.MemoryModule;

/**
 * Represents the MBC3 (Memory Bank Controller) one of many types of controller
 * used in the Game Boy
 *
 * @author rodrigotimoteo
 */

public class MBC3 extends MemoryModule implements RomModule {

    /**
     * Stores the number of ram banks used by this rom
     */
    private final int numberOfRamBanks;

    /**
     * Stores whether ram is enabled or not
     */
    boolean ramEnable = false;

    /**
     * Creates a new Memory Module based on rom content
     *
     * @param romBanks number of Rom banks
     * @param romContent byte array containing the rom's content
     */
    public MBC3(int romBanks, int ramBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);

        numberOfRamBanks = ramBanks;
    }

    /**
     * Deals with special writes to the rom registers (used to manage the
     * memory bank controller settings)
     *
     * @param address where memory should be changed
     * @param value   to assign
     */
    @Override
    public void setValue(int address, int value) {
//        if      (address < RAM_ENABLE_END)
//            ramEnable = (value & 0x0A) == 0x0A;
//
//        else if (address < ROM_BANK_NUMBER_END)
//            if(value != 0)
//                changeActiveBank(value & );
//
//            else if (address < RAM_BANK_NUMBER_END)
//
//
//        else if (address < BANK_MODE_END)


    }

    /**
     * Method to check the status of the boolean variable that check whether ram
     * is enable
     *
     * @return ram enable variable (true if enable false otherwise)
     */
    @Override
    public boolean getRamStatus() {
        return ramEnable;
    }

    /**
     * Method to get the number of ram banks used by this specific rom
     *
     * @return number of ram banks
     */
    @Override
    public int getRamBanks() {
        return numberOfRamBanks;
    }
}
