package com.example;

import java.awt.*;

public class Paddle extends Rectangle { //наследник класса Rectangle


    private double velocity = 0.0; //скорость движения равна нулю
    //конструктор с параметрами х, у
    public Paddle(double x, double y) {
        this.x = x;
        this.y = y;
        this.sizeX = Const.PADDLE_WIDTH;
        this.sizeY = Const.PADDLE_HEIGHT;
    }
    //обновление
    public void update() {

        x += velocity * Const.FT_STEP;
    }
    //остоновка
    public void stopMove() {

        velocity = 0.0; //скорость равна нулю
    }
    //движение влево
    public void moveLeft() {

        if (left() > 0.0) {
            velocity = -Const.PADDLE_VELOCITY;
        } else {
            velocity = 0.0; //стоит на месте
        }
    }
    //движение вправо
    public void moveRight() {
        if (right() < Const.SCREEN_WIDTH) {
            velocity = Const.PADDLE_VELOCITY;
        } else {
            velocity = 0.0; //стоит на месте
        }
    }
    //отрисовка платформы
    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) (left()), (int) (top()), (int) sizeX, (int) sizeY);
    }

}
