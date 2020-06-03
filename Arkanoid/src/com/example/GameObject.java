package com.example;
//абстрактный класс для определения границ объектов
public abstract class GameObject {
    abstract double left(); //левая

    abstract double right(); //правая

    abstract double top(); //верхняя

    abstract double bottom(); //нижняя
}
