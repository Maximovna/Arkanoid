package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;

import static com.example.Arkanoid.*;


//мяч

public class Ball extends GameObject {

    public double x;
    public double y;
    private final double radius = BALL_RADIUS;
    public double velocityX = BALL_VELOCITY;
    public double velocityY = BALL_VELOCITY;
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
        x += velocityX * FT_STEP;
        y += velocityY * FT_STEP;

        if (left() < 0) //если граница мяча дальше границы окна
            velocityX = BALL_VELOCITY; //мяч отталкичается от левой границы
        else if (right() > SCREEN_WIDTH) //иначе, если граница мяча дальше границы окна
            velocityX = -BALL_VELOCITY; //мяч отталкивается от правой границы
        if (top() < 0) { //если граница мяча меньше нуля
            velocityY = BALL_VELOCITY; //мяч отталкивается
        } else if (bottom() > SCREEN_HEIGHT) { //иначе, граница мяча ниже границы окна
            velocityY = -BALL_VELOCITY;
            x = paddle.x;
            y = paddle.y - 50;
            scoreBoard.die(); //мяч умирает
        }

    }
    //переопределение метода родительского класса
    public double left() {

        return x - radius;
    }
    //переопределение метода родительского класса
    public double right() {

        return x + radius;
    }
    //переопределение метода родительского класса
    public double top() {

        return y - radius;
    }
    //переопределение метода родительского класса
    public double bottom() {

        return y + radius;
    }


}
