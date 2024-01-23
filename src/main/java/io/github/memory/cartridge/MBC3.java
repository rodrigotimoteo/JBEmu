package io.github.memory.cartridge;

import io.github.memory.MemoryModule;

/**
 * Represents the MBC3 (Memory Bank Controller) one of many types of controller
 * used in the Game Boy
 *
 * @author rodrigotimoteo
 */

public class MBC3 extends MemoryModule {

    public MBC3(int romBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);
    }
}
