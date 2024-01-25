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

    private Bus bus;

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

        File rom = new File("/Users/rodrigotimoteo/Projects/JBoyEmu/testRoms/07-jr,jp,call,ret,rst.gb");

        if (!rom.exists())
            throw new IllegalArgumentException("Invalid Path");

        romReader.loadRom(rom);

        cgb = romReader.isCGB();

        bus = new Bus(cgb, romReader.getModule());
        cpu = new CPU(bus);
        ppu = new PPU(bus);

        bus.setCpu(cpu);
        bus.setPpu(ppu);
    }

    @Override
    public void run() {
        cpu.tick();
        //ppu.tick();
        while(true) {
            int machineCycles = cpu.getTimers().getMachineCycles();
            cpu.tick();
        }
    }
}
