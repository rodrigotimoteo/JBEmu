package io.github.memory.cartridge;

import io.github.memory.MemoryManager;
import io.github.memory.MemoryModule;

/**
 * Represents the MBC0 (Memory Bank Controller) one of many types of controller
 * used in the Game Boy this controller makes use of a normally a single 32KiB
 * rom and if ram exists it will have a 8KiB module
 *
 * @author rodrigotimoteo
 */

public class MBC0 extends MemoryModule implements RomModule {

    /**
     * Stores the number of ram banks used by this rom
     */
    private final int numberOfRamBanks;

    /**
     * Creates a new Memory Module based on rom content
     *
     * @param romBanks number of Rom banks
     * @param romContent byte array containing the rom's content
     */
    public MBC0(int romBanks, int ramBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);

        numberOfRamBanks = ramBanks;
    }

    /**
     * Method to check the status of the boolean variable that check whether ram
     * is enable
     *
     * @return always true due to this method only being called in the event
     * that ram exists and is available (this controller cannot have more than
     * one bank by definition)
     */
    @Override
    public boolean getRamStatus() {
        return true;
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
