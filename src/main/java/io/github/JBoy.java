package io.github;

import io.github.cpu.CPU;
import io.github.display.Controller;
import io.github.display.Display;
import io.github.memory.Bus;
import io.github.memory.cartridge.RomReader;
import io.github.ppu.PPU;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Responsible for running an instance of the GB emulator
 *
 * @author rodrigotimoteo
 */

public class JBoy extends Thread {

    /**
     * Stores whether to run in Color Game Boy mode
     */
    private final boolean cgb;

    /**
     * Stores a reference to the bus responsible for handling components
     * communication
     */
    private final Bus bus;

    /**
     * Stores a reference to the cpu
     */
    private final CPU cpu;

    /**
     * Stores a reference to the ppu
     */
    private final PPU ppu;

    /**
     * Stores a reference to the display class (gui management)
     */
    private final Display display;

    /**
     * Stores a reference to the display's key listener
     */
    private final Controller controller;

    /**
     * This method creates a new emulator instance and its interface
     *
     * @throws IOException              failed to load rom
     * @throws IllegalArgumentException rom path is invalid
     */
    public JBoy() throws IOException, IllegalArgumentException {
        RomReader romReader = new RomReader();

        File rom = new File("/Users/rodrigotimoteo/Projects/JBoyEmu/testRoms/11-op a,(hl).gb");

        if (!rom.exists())
            throw new IllegalArgumentException("Invalid Path");

        romReader.loadRom(rom);

        cgb = romReader.isCGB();

        bus        = new Bus(cgb, romReader.getModule());
        cpu        = new CPU(bus);
        ppu        = new PPU(bus);
        display    = new Display(bus);
        controller = new Controller(bus);

        bus.setCpu(cpu);
        bus.setPpu(ppu);
        bus.setDisplay(display);
        bus.setController(controller);

        startDisplay();
    }

    /**
     * Method that initializes the Display and assigns it a Controller
     */
    private void startDisplay() {
        display.setPreferredSize(new Dimension(Display.WIDTH, Display.HEIGHT));

        JFrame newWindow = new JFrame("JBEmu");
        newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newWindow.setLocationRelativeTo(null);
        newWindow.setContentPane(display);
        newWindow.setResizable(true);
        newWindow.setVisible(true);
        newWindow.pack();

        newWindow.addKeyListener(controller);
    }

    /**
     * Main loop of the emulator thread
     */
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
