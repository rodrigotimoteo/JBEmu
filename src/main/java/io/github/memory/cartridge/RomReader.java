package io.github.memory.cartridge;

import io.github.memory.MemoryModule;
import io.github.memory.ReservedAddresses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private final HashMap<Integer,Integer> romBanksMap = new HashMap<>();
    {
        romBanksMap.put(0, 2);
        romBanksMap.put(1, 4);
        romBanksMap.put(2, 8);
        romBanksMap.put(3, 16);
        romBanksMap.put(4, 32);
        romBanksMap.put(5, 64);
        romBanksMap.put(6, 128);
        romBanksMap.put(7, 256);
        romBanksMap.put(8, 512);
    }

    /**
     * Number of RAM Banks, for a given input RAM size
     */


    /**
     * Stores the entirety of the ROM's content
     */
    private byte[] romContent;

    /**
     * Loads a Rom from a given File into the romContent variable
     *
     * @param romFile to load
     * @throws IOException if there is an error reading the Rom
     */
    public void loadRom(File romFile) throws IOException{
        try {
            romContent = Files.readAllBytes(romFile.toPath());
        } catch (IOException ioException) {
            throw new IOException("Error Reading ROM");
        }
    }

    /**
     * Returns the Rom Title contained inside the Rom (with its character
     * limitations)
     *
     * @return String with the title
     */
    public String getRomTitle() {
        StringBuilder title = new StringBuilder();

        for(int i = ReservedAddresses.TITLE_START.getAddress(); i <= ReservedAddresses.TITLE_END.getAddress(); i++) {
            if(romContent[i] != 0)
                title.append((char) romContent[i]);
            else break;
        }

        return title.toString();
    }

    /**
     * Checks if the Rom is a CGB or DMG Rom, to define the mode in which to run
     *
     * @return boolean (true if CGB mode false otherwise)
     */
    public boolean isCGB() {
        return (romContent[ReservedAddresses.CONSOLE_TYPE.getAddress()] & 0xff) == 0x80;
    }

    /**
     * Converts a byte array representing a rom to memory module
     *
     *
     * @return Memory Module as MBC
     */
    public MemoryModule getModule() {
        int cartridgeType = romContent[ReservedAddresses.CARTRIDGE_TYPE.getAddress()];

        switch (cartridgeType) {
            case 0x00, 0x08, 0x09 -> {
                return new MBC0(getRomSize(), romContent);
            }
            case 0x01, 0x02, 0x03 -> { //MBC1

            }
            case 0x05, 0x06 -> { //MBC2

            }
            case 0x0F, 0x10, 0x11, 0x12, 0x13 -> { //MBC3

            }
            case 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E -> { //MBC5

            }
        };

        return null;
    }

    public int getRomSize() {
        int romSize = romContent[ReservedAddresses.ROM_SIZE.getAddress()];

        return romBanksMap.get(romSize);
    }
}
