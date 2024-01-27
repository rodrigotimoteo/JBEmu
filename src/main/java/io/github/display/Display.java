package io.github.display;

import io.github.memory.Bus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel {

    /**
     * Constants denoting the size of the drawn Game Boy resolution
     */
    public static final int WIDTH  = 160;
    public static final int HEIGHT = 144;

    /**
     * Stores a reference to the bus
     */
    private final Bus bus;

    /**
     * Stores the display's buffered image
     */
    private final BufferedImage bufferedImage;

    /**
     *
     *
     * @param bus reference to this instances bus
     */
    public Display(Bus bus) {
        super();
        GraphicsConfiguration configuration = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        this.bus = bus;
        bufferedImage = configuration.createCompatibleImage(WIDTH, HEIGHT);
    }

    /**
     * Buffered Image getter method
     *
     * @return buffered image object
     */
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
