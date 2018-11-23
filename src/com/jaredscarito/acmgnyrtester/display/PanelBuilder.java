package com.jaredscarito.acmgnyrtester.display;

import javax.swing.*;

/**
 * Created by TheWolfBadger on 11/22/18.
 */
public class PanelBuilder extends JPanel {
    public PanelBuilder(String name, int locX, int locY, int width, int height) {
        setName(name);
        setBounds(locX, locY, width, height);
        setSize(width, height);
    }
}
