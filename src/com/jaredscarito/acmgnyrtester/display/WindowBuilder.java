package com.jaredscarito.acmgnyrtester.display;

import javax.swing.*;
import java.awt.*;

/**
 * Created by TheWolfBadger on 11/22/18.
 */
public class WindowBuilder extends JFrame {
    public WindowBuilder(LayoutManager layout, int width, int height) {
        setLayout(layout);
        setSize(width, height);
        setResizable(false);
    }
}
