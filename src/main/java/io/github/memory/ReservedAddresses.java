package io.github.memory;

/**
 * Used to keep track of all the reserved address, that serve direct
 * utility to the memory management
 *
 * @author rodrigotimoteo
 */

public enum ReservedAddresses {

    TITLE_START     (0x0134),
    TITLE_END       (0x0142),
    CONSOLE_TYPE    (0x0143),
    CARTRIDGE_TYPE  (0x0147),
    ROM_SIZE        (0x0148),
    RAM_SIZE        (0x0149),
    ROM_BANK0_END   (0x4000),
    SWITCH_ROM_END  (0x8000),
    TILE_DATA_1     (0x8800),
    TILE_DATA_2     (0x9000),
    TILE_MAP_0      (0x9800),
    TILE_MAP_1      (0x9C00),
    VRAM_END        (0xA000),
    ERAM_END        (0xC000),
    WRAM_END        (0xE000),
    OAM_START       (0xFE00),
    OAM_END         (0xFEA0),
    IO_START        (0xFF00),
    SB              (0xFF01),
    SC              (0xFF02),
    DIV             (0xFF04),
    TIMA            (0xFF05),
    TMA             (0xFF06),
    TAC             (0xFF07),
    IF              (0xFF0F),
    NR10            (0xFF10),
    NR11            (0xFF11),
    NR12            (0xFF12),
    NR13            (0xFF13),
    NR14            (0xFF14),
    NR21            (0xFF16),
    NR22            (0xFF17),
    NR23            (0xFF18),
    NR24            (0xFF19),
    NR30            (0xFF1A),
    NR31            (0xFF1B),
    NR32            (0xFF1C),
    NR33            (0xFF1D),
    NR34            (0xFF1E),
    NR41            (0xFF20),
    NR42            (0xFF21),
    NR43            (0xFF22),
    NR44            (0xFF23),
    NR50            (0xFF24),
    NR51            (0xFF25),
    NR52            (0xFF26),
    WAVE_START      (0xFF30),
    WAVE_END        (0xFF3F),
    LCDC            (0xFF40),
    STAT            (0xFF41),
    SCY             (0xFF42),
    SCX             (0xFF43),
    LY              (0xFF44),
    LYC             (0xFF45),
    DMA             (0xFF46),
    BGP             (0xFF47),
    OBP0            (0xFF48),
    OBP1            (0xFF49),
    WY              (0xFF4A),
    WX              (0xFF4B),
    IE              (0xFFFF);

    /**
     * Stores the address assigned to specific constant
     */
    private final int address;

    /** Constructor Method
     *
     * <p>Responsible for assigning the value to the given constant name</p>
     *
     * @param address to attribute to constant
     */
    ReservedAddresses(int address) {
        this.address = address;
    }

    /**
     * Getter for the value assigned to the constants
     *
     * @return the value of the address
     */
    public int getAddress() {
        return address;
    }

}
