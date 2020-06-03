package com.example;

import java.awt.*;

import static com.example.Arkanoid.*;

//платформа
public class Paddle extends Rectangle { //наследник класса Rectangle


    private double velocity = 0.0; //скорость движения равна нулю
    //конструктор с параметрами х, у
    public Paddle(double x, double y) {
        this.x = x;
        this.y = y;
        this.sizeX = PADDLE_WIDTH;
        this.sizeY = PADDLE_HEIGHT;
    }
    //обновление
    public void update()

    {
        x += velocity * FT_STEP; //направление скорости
    }
    //остоновка
    public void stopMove() {

        velocity = 0.0;
    }
    //движение влево
    public void moveLeft() {
        if (left() > 0.0) {
            velocity = -PADDLE_VELOCITY;
        } else {
            velocity = 0.0; //стоит на месте
        }
    }
    //движение вправо
    public void moveRight() {
        if (right() < SCREEN_WIDTH) { //если сдвиг вправо
            velocity = PADDLE_VELOCITY; //направление движения
        } else {
            velocity = 0.0; //стоит на месте
        }
    }
    //отрисовка платформы
    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) (left()), (int) (top()), (int) sizeX, (int) sizeY); //границы
    }

}
