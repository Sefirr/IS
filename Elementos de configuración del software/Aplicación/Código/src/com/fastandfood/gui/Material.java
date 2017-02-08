package com.fastandfood.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Provides common dimensions and material language for the UI
 *
 * @author Borja
 */
public class Material {

    static Dimension SCREEN_DIMENSION           = new Dimension(getScreenWorkingWidth() - 100, getScreenWorkingHeight() - 35);
    public static Dimension ACTION_DIMENSION    = new Dimension(500, 200);
    public static Dimension BIG_ACTION_DIMENSION = new Dimension(600, 500);
    static Dimension LOGIN_TEXTFIELDSIZE        = new Dimension(300, 60);
    public static Dimension TEXTFIELDSIZE       = new Dimension(160, 20);

    static String FF_LOGO = "com/fastandfood/gui/res/fflogo.png";

    private static int getScreenWorkingWidth() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }

    private static int getScreenWorkingHeight() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
