package io.github.memory.cartridge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RomReaderTest {

    File file = new File("/Users/rodrigotimoteo/Projects/JBoyEmu/testRoms/01-special.gb");

    RomReader romReader;

    @BeforeEach
    void setUp() {
        romReader = new RomReader();
    }

    @Test
    void testLoadRom() throws IOException {
        romReader.loadRom(file);

        assertInstanceOf(RomModule.class, romReader.getModule());
        assertNotEquals(romReader.getRomSize(), 0);
        assertEquals(romReader.getRamSize(), 0);
    }


}