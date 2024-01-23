package io.github.memory.cartridge;

import io.github.memory.MemoryManager;

/**
 * Defines specific behaviour to all the memory modules responsible for handling
 * roms, memory bank controllers specifically
 *
 * @author rodrigotimoteo
 */

public interface RomModule {

    /**
     * Method to check the status of the boolean variable that check whether ram
     * is enable
     *
     * @return ram enable variable (true if enable false otherwise)
     */
    boolean getRamStatus();

    /**
     * Method to get the number of ram banks used by this specific rom
     *
     * @return number of ram banks
     */
    int getRamBanks();
}
