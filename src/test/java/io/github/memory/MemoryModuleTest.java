package io.github.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MemoryModuleTest {

    MemoryModule moduleTest1;
    MemoryModule moduleTest2;
    MemoryModule moduleTest3;

    @BeforeEach
    void init() {
        moduleTest1 = new MemoryModule(0x10, 0x20);
        moduleTest2 = new MemoryModule(0x10, 1, 0x20, 2);
        moduleTest3 = new MemoryModule(0x10, 2, 0x20, 2);
    }

    /**
     * Tests the cases where we use 1 banks and 1 simultaneous bank as well as
     * reading and writing from it
     *
     * @param address where to read and write from
     */
    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x29, 0x28})
    void test1Bank(int address) {
        moduleTest1.setValue(address, address);

        assertEquals(moduleTest1.getValue(address), address);
    }

    /**
     * Tests the cases where we use 2 banks (or more) and 1 simultaneous bank
     * as well as reading and writing from it
     *
     * @param address where to read and write from
     */
    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x29, 0x28})
    void test2Bank1Simultaneous0Active(int address) {
        moduleTest2.setValue(address, address);

        assertEquals(moduleTest2.getValue(address), address);
    }

    /**
     * Tests the cases where we use 2 banks (or more) and 1 simultaneous bank
     * (changing it to bank 1 instead of 0) as well as reading and writing from
     * it
     *
     * @param address where to read and write from
     */
    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x29, 0x28})
    void test2Bank1Simultaneous1Active(int address) {
        moduleTest2.changeActiveBank(1);

        moduleTest2.setValue(address, address);

        assertEquals(moduleTest2.getValue(address), address);
    }

    /**
     * Tests the cases where we use 2 banks (or more) and 1 simultaneous bank
     * (changing it to bank 1 instead of 0 and later back to 0 for error
     * checking) as well as reading and writing from it
     *
     * @param address where to read and write from
     */
    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x29, 0x28})
    void test2Bank1Simultaneous1ActiveChecking0(int address) {
        moduleTest2.changeActiveBank(1);

        moduleTest2.setValue(address, address);

        moduleTest2.changeActiveBank(0);

        assertEquals(moduleTest2.getValue(address), 0x00);
    }

    /**
     * Tests the cases where we use 2 banks (or more) and two simultaneous banks
     * (or more) as well as reading and writing from both banks
     *
     * @param address where to read and write from
     */
    @ParameterizedTest
    @ValueSource(ints = {0x20, 0x29, 0x28, 0x30, 0x39, 0x35})
    void test2Bank2Simultaneous(int address) {
        moduleTest3.setValue(address, address);

        assertEquals(moduleTest3.getValue(address), address);
    }
}