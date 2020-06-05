package com.example;

import java.awt.*;

public class Ball extends GameObject {

    public double x;
    public double y;
    private final double radius = Const.BALL_RADIUS;
    public double velocityX = Const.BALL_VELOCITY;
    public double velocityY = Const.BALL_VELOCITY;
    //конструктор с параметрами х, у
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //отрисовка мяча
    void draw(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) left(), (int) top(), (int) radius * 2, (int) radius * 2);

    }
    //обновление
    public void update(ScoreBoard scoreBoard, Paddle paddle) {
        x += velocityX * Const.FT_STEP;
        y += velocityY * Const.FT_STEP;

        if (left() < 0) //если граница мяча дальше границы окна
            velocityX = Const.BALL_VELOCITY; //мяч отталкичается от левой границы
        else if (right() > Const.SCREEN_WIDTH) //иначе, если граница мяча дальше границы окна
            velocityX = -Const.BALL_VELOCITY; //мяч отталкивается от правой границы
        if (top() < 0) { //если граница мяча меньше нуля
            velocityY = Const.BALL_VELOCITY; //мяч отталкивается
        } else if (bottom() > Const.SCREEN_HEIGHT) { //иначе, граница мяча ниже границы окна
            velocityY = -Const.BALL_VELOCITY;
            x = paddle.x;
            y = paddle.y - 50;
            scoreBoard.die(); //мяч умирает
        }

    }
    //переопределение метода родительского класса
    @Override
    public double left() {

        return x - radius;
    }
    //переопределение метода родительского класса
    @Override
    public double right() {

        return x + radius;
    }
    //переопределение метода родительского класса
    @Override
    public double top() {

        return y - radius;
    }
    //переопределение метода родительского класса
    @Override
    public double bottom() {

        return y + radius;
    }


}
