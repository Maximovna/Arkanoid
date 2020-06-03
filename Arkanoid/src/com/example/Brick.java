package com.example;

import java.awt.*;

import static com.example.Arkanoid.BLOCK_HEIGHT;
import static com.example.Arkanoid.BLOCK_WIDTH;

public class Brick extends Rectangle { //наследник класса Rectangle

    boolean destroyed = false;
    //конструктор с парамерами х, у
    Brick(double x, double y) {
        this.x = x;
        this.y = y;
        this.sizeX = BLOCK_WIDTH;
        this.sizeY = BLOCK_HEIGHT;
    }
    //отрисовка "кирпича"
    void draw(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) left(), (int) top(), (int) sizeX, (int) sizeY);

    }
}
