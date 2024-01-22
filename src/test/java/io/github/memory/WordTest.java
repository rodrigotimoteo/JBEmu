package io.github.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    Word word;

    @BeforeEach
    void init() {
        word = new Word();
    }

    @Test
    void defaultMemoryIs0x00() {
        assertEquals(word.getValue(), 0);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void setBitOf0x00(int bit) {
        word.setBit(bit);

        assertEquals(word.getValue(), (0x01 << bit) & 0xFF);
    }
}