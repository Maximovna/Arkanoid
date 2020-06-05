package com.example;

import java.awt.*;

public class Brick extends Rectangle {

    boolean destroyed = false;
    //конструктор с парамерами х, у
    public Brick(double x, double y) {
        this.x = x;
        this.y = y;
        this.sizeX = Const.BLOCK_WIDTH;
        this.sizeY = Const.BLOCK_HEIGHT;
    }

    protected void draw(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) left(), (int) top(), (int) sizeX, (int) sizeY);

    }
}
