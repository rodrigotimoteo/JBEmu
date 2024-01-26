package io.github.ppu;

import io.github.memory.Bus;

/**
 *
 *
 * @author rodrigotimoteo
 */

public class Drawer {

    /**
     *
     */
    private final Bus bus;

    /**
     * Stores the matrix used to keep the pixels to form each frame
     */
    private final int[][] colorMatrix;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Drawer(Bus bus, int[][] colorMatrix) {
        this.bus = bus;
        this.colorMatrix = colorMatrix;
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
