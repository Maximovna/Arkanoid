package com.example;

public class Rectangle extends GameObject { //наследник класса GameObject

    public double x, y;
    public double sizeX;
    public double sizeY;
    //переопределение метода родительского класса
    public double left() {

        return x - sizeX / 2.0;
    }
    //переопределение метода родительского класса
    public double right() {

        return x + sizeX / 2.0;
    }
    //переопределение метода родительского класса
    public double top() {

        return y - sizeY / 2.0;
    }
    //переопределение метода родительского класса
    public double bottom() {

        return y + sizeY / 2.0;
    }

}
