package io.github.memory.cartridge;

import java.util.HashMap;

/**
 * Responsible for reading the ROM and assigning the content to the desired
 * module to be read from
 *
 * @author rodrigotimoteo
 */

public class RomReader {

    /**
     * Number of ROM Banks, for a given input ROM size
     */
    public final HashMap<Integer,Integer> romBanksMap = new HashMap<>();
    {
        romBanksMap.put(0, 2);
        romBanksMap.put(1, 4);
        romBanksMap.put(2, 8);
        romBanksMap.put(3, 16);
        romBanksMap.put(4, 32);
        romBanksMap.put(5, 64);
        romBanksMap.put(6, 128);
        romBanksMap.put(0x52, 72);
        romBanksMap.put(0x53, 80);
        romBanksMap.put(0x54, 96);
    }

    /**
     * Number of RAM Banks, for a given input RAM size
     */
    public final HashMap<Integer,Integer> ramBanksMap = new HashMap<>();
    {
        ramBanksMap.put(0, 0);
        ramBanksMap.put(1, 1);
        ramBanksMap.put(2, 1);
        ramBanksMap.put(3, 4);
        ramBanksMap.put(4, 16);
    }




}
