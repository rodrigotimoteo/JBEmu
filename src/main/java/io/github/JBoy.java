package io.github;

import io.github.cpu.CPU;
import io.github.memory.Bus;
import io.github.memory.cartridge.RomReader;
import io.github.ppu.PPU;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for running an instance of the GB emulator
 *
 * @author rodrigotimoteo
 */

public class JBoy extends Thread {

    private boolean cgb;

    private CPU cpu;

    private PPU ppu;


    /** Constructor Method
     *
     * <p>This method creates a new emulator instance and its interface</p>
     *
     * @throws IOException failed to load rom
     */
    public JBoy() throws IOException {
        RomReader romReader = new RomReader();

        File rom = new File("/Users/rodrigotimoteo/Projects/JBoyEmu/testRoms/01-special.gb");
        romReader.loadRom(rom);

        cgb = romReader.isCGB();

        Bus bus = new Bus(cgb, romReader.getModule());
        CPU cpu = new CPU(bus);
        PPU ppu = new PPU(bus);

        bus.setCpu(cpu);
        bus.setPpu(ppu);
    }





    @Override
    public void run() {

    }
}
