package io.github.memory.cartridge;

import io.github.memory.MemoryModule;

/**
 * Represents the MBC0 (Memory Bank Controller) one of many types of controller
 * used in the Game Boy this controller makes use of a normally a single 32KiB
 * rom and if ram exists it will have a 8KiB module
 *
 * @author rodrigotimoteo
 */

public class MBC0 extends MemoryModule {

    /**
     * Constructor Method
     *
     * <p>Creates a new Memory Module based on rom content</p>
     *
     * @param romBanks number of Rom banks
     * @param romContent full byte array containing the rom's content
     */
    public MBC0(int romBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);
    }

}
