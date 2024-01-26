package io.github.ppu;

import io.github.memory.Bus;
import io.github.memory.ReservedAddresses;
import io.github.memory.Word;

/**
 *
 *
 * @author rodrigotimoteo
 */

public class Registers {

    /**
     *
     */
    private final Bus bus;

    /**
     * Indicates whether it's intended to run on Color Game Boy mode or not
     */
    private final boolean cgb;

    /**
     * Stores the LCDC memory module directly in the registers for easy access,
     * this register is used to test for control information. Bit -> Meaning
     * bit 7 -> LCD and PPU enabled     (0 -> Off, 1 -> On)
     * bit 6 -> Window Tile Map         (0 -> 9800-9BFF, 1 -> 9C00-9FFF)
     * bit 5 -> Window enable           (0 -> Off, 1 -> On)
     * bit 4 -> BG and Window Tile Data (0 -> 8800-97FF, 1 -> 8000-8FFF)
     * bit 3 -> BG Tile Map             (0 -> 9800-9BFF, 1 -> 9C00-9FFF)
     * bit 2 -> OBJ size                (0 -> 8x8, 1 -> 8x16)
     * bit 1 -> OBJ enabled             (0 -> Off, 1 -> On)
     * bit 0 -> BG and Window/Priority  (0 -> Off, 1 -> On)
     */
    private final Word LCDC;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Registers(Bus bus) {
        this.bus  = bus;
        this.cgb  = bus.isCgb();
        this.LCDC = bus.getWord(ReservedAddresses.LCDC.getAddress());

    }

    public boolean isTurnedOn() {
        return LCDC.testBit(7);
    }
}
