package io.github.memory;

/**
 * Responsible for interactions between the various system components and
 * storing the main memory
 *
 * @author rodrigotimoteo
 */


public class Bus {

    private class Memory {

        private final MemoryModule rom;
//        private final MemoryModule vram;
//        private final MemoryModule eram;
//        private final MemoryModule wram;
        private final MemoryModule oam;
        private final MemoryModule bottomRegisters;

        public Memory(MemoryModule rom) {
            this.rom = rom;




//            wram = new MemoryModule()
            oam = new MemoryModule(0xA0, ReservedAddresses.OAM_START.getAddress());
            bottomRegisters = new MemoryModule(0x100, ReservedAddresses.IO_START.getAddress());
        }



    }





}
