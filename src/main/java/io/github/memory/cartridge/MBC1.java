package io.github.memory.cartridge;

import io.github.memory.MemoryManager;
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

public class MBC1 extends MemoryModule implements RomModule {

    /**
     * Stores the end of the ram enable register
     */
    private final int RAM_ENABLE_END = 0x2000;

    /**
     * Stores the end of the rom bank selector register
     */
    private final int ROM_BANK_NUMBER_END = 0x4000;

    /**
     * Stores the end of the ram bank selector register. This register can also
     * be used in bigger roms to select the upper bits of the rom bank number
     */
    private final int RAM_BANK_NUMBER_END = 0x6000;

    /**
     * Stores the end of the banking mode selector register
     */
    private final int BANK_MODE_END = 0x8000;

    /**
     * Stores the number of ram banks used by this rom
     */
    private final int numberOfRamBanks;

    /**
     * Stores whether ram is enabled or not
     */
    private boolean ramEnable = false;

    /**
     * Stores the banking mode in use
     * False it's the default mode where 0000-3FFF and A000-BFFF are locked into
     * bank 0 of rom and eram respectively, if it's true 0000-3FFF and A000-BFFF
     * can be bank-switched via the 4000-5FFF register
     */
    private boolean bankingMode = false;

    /**
     * Stores if the rom is big enough or if the ram is big enough to check the
     * 4000-5FFF register
     */
    private final boolean bigEnoughRom;

    //LACKS IMPLEMENTATION FOR NOW
    /**
     * If banking mode is changed to true this will define which bank will be
     * accessed at 0000-3FFF
     */
    private int romBankIfBankingModeTrue = 0;

    /**
     * Creates a new Memory Module based on rom content
     *
     * @param romBanks number of rom banks to create
     * @param romContent byte array containing the rom's content
     */
    public MBC1(int romBanks, int ramBanks, byte[] romContent) {
        super(romContent, 0x4000, 2, 0x0, romBanks);

        numberOfRamBanks = ramBanks;

        bigEnoughRom = numberOfRamBanks > 1 && getRomBanks() > 16;
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
        int numberOfBanks = getRomBanks();

        if      (address < RAM_ENABLE_END)
            ramEnable = (value & 0x0A) == 0x0A;

        else if (address < ROM_BANK_NUMBER_END)
            if(value != 0)
                if(numberOfBanks > 16)
                    changeActiveBank(value & (numberOfBanks - 1));
                else
                    changeActiveBank(value & (numberOfBanks - 1));
            else
                changeActiveBank(1);

        else if (address < RAM_BANK_NUMBER_END)
            System.out.println("no implementation");
//            if(numberOfRamBanks != 1)
//                bus.changeRamBank();

        else if (address < BANK_MODE_END && bigEnoughRom)
            bankingMode = (value & 0x01) != 0;

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
