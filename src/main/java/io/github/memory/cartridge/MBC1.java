package io.github.memory.cartridge;

import io.github.memory.MemoryModule;

/**
 * <p>Represents the MBC1 (Memory Bank Controller) one of many types of controller
 * used in the Game Boy this controller has up to 2MByte rom and/or 32KiB ram.
 * In the default configuration it supports up to 512KiB rom and 32KiB banked
 * ram, but can also support 2MByte rom by sacrificing to use only 8KiB of ram.</p>
 *
 * <p>0000-7FFF is used to read from rom and write to MBC control registers</p>
 *
 * @author rodrigotimoteo
 */

public class MBC1 extends MemoryModule {

    public MBC1(int romBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);
    }
}
