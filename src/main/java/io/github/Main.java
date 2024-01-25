package io.github;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JBEmu");

        try {
            JBoy jBoy = new JBoy();
            jBoy.start();
        } catch (IOException ignored) {

        }
    }
}