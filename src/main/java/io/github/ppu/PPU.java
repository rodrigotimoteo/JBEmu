package io.github.ppu;

import io.github.memory.Bus;

public class PPU {

    private final Bus bus;

    private final boolean cgb;

    public PPU(Bus bus) {
        this.bus = bus;

        cgb = bus.isCgb();
    }

}
