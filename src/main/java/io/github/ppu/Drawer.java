package io.github.ppu;

import io.github.memory.Bus;

import java.awt.image.BufferedImage;

/**
 *
 *
 * @author rodrigotimoteo
 */

public class Drawer {

    /**
     * Stores a reference to the bus
     */
    private final Bus bus;

    /**
     * Stores the matrix used to keep the pixels to form each frame
     */
    private final BufferedImage bufferedImage;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Drawer(Bus bus) {
        this.bus = bus;
        this.bufferedImage = bus.getImage();
    }

    /**
     * Method responsible for drawing the background of a frame
     */
    public void drawBackground() {

    }

    /**
     * Method responsible for drawing the window of a frame
     */
    public void drawWindow() {

    }

    /**
     * Method responsible for drawing the sprites of a frame
     */
    public void drawSprites() {

    }
}
