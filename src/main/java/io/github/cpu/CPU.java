package io.github.cpu;

import io.github.memory.Bus;

/**
 * Represents the Central Processing Unit of the emulator an 8-bit 8080-like
 * Sharp CPU
 *
 * @author rodrigotimoteo
 */

public class CPU {

    private final Bus bus;

    private final boolean cgb;

    public CPU(Bus bus) {
        this.bus = bus;

        cgb = bus.isCgb();
    }


}
