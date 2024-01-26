package io.github.display;

import io.github.memory.Bus;
import io.github.memory.ReservedAddresses;
import io.github.memory.Word;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {

    /**
     * Stores a reference to the bus responsible for handling components
     * communication
     */
    private final Bus bus;

    /**
     *
     */
    private final Word joypad;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Controller(Bus bus) {
        this.bus = bus;

        joypad = bus.getWord(ReservedAddresses.JOYP.getAddress());
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
