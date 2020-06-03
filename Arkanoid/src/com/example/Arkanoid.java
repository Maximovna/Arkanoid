package com.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

public class Arkanoid extends JFrame implements KeyListener {

    /* Константы */
    public static final int SCREEN_WIDTH = 800; //нижняя граница
    public static final int SCREEN_HEIGHT = 600; //верхняя граница
    public static final double BLOCK_WIDTH = 60.0; //ширина блока
    public static final double BLOCK_HEIGHT = 20.0; //высота блока
    public static final int COUNT_BLOCKS_X = 11; //количество блоков в строке
    public static final int COUNT_BLOCKS_Y = 4; //количество блоков в столбце
    public static final double PADDLE_WIDTH = 80.0; //ширина платформы
    public static final double PADDLE_HEIGHT = 20.0; //высота платформы
    public static final double PADDLE_VELOCITY = 0.6; //скорость движения платформы
    public static final double BALL_RADIUS = 10.0; //радиус шарика
    public static final double BALL_VELOCITY = 0.3; //скорость движения шарика
    public static final int PLAYER_LIVES = 5; //количество жизней
    public static final double FT_SLICE = 1.0; //ФУТОВЫЙ КУСОК
    public static final double FT_STEP = 1.0; //ФУТОВЫЙ ШАГ
    private static final String FONT = "Courier New"; //шрифт текста

    /* Переменные игры */

    private boolean tryAgain = false; //поцесс идет заново

    private final Paddle paddle = new Paddle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 50); // начальное положение платформы
    private final Ball ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); //начальное положения мяча
    private static final List<Brick> bricks = new ArrayList<>();
    private final ScoreBoard scoreboard = new ScoreBoard(FONT);

    private double lastFt;
    private double currentSlide; //число текущего слайда

    public Arkanoid() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //запрещает изменять размер окна
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT); //размер окна
        setVisible(true); //видимость
        addKeyListener(this); //ключевой слушатель
        setLocationRelativeTo(null); //расположение по центру
        setTitle("Arkanoid"); //Заголовок

        createBufferStrategy(2); //БуфферСтратегия обновления окна
        initializeBricks(bricks); //инициализация "кирпичей"

    }


    public static void main(String[] args) {

        new Arkanoid().run(bricks);
    }
    //(полиморфизм: использование абстрактного типа GameObject mA, GameObject mB)
    public boolean isIntersecting(GameObject mA, GameObject mB) {//пересечение
        return !(mA.right() >= mB.left()) || !(mA.left() <= mB.right())
                || !(mA.bottom() >= mB.top()) || !(mA.top() <= mB.bottom());
    }

    public void testCollision(Paddle mPaddle, Ball mBall) {//Проверка на столкновение платформа - шар
        if (isIntersecting(mPaddle, mBall)) //если платфома и шар пересекаются
            return;
        mBall.velocityY = -BALL_VELOCITY;
        if (mBall.x < mPaddle.x)
            mBall.velocityX = -BALL_VELOCITY;
        else
            mBall.velocityX = BALL_VELOCITY;
    }

    public void testCollision(Brick mBrick, Ball mBall) {// Проверка на столкновение для "кирпич" - шар
        if (isIntersecting(mBrick, mBall)) return;

        mBrick.destroyed = true; //кирпич разбит
        scoreboard.increaseScore(); //увеличивается счет

        double overlapLeft = mBall.right() - mBrick.left();
        double overlapRight = mBrick.right() - mBall.left();
        double overlapTop = mBall.bottom() - mBrick.top();
        double overlapBottom = mBrick.bottom() - mBall.top();

        boolean ballFromLeft = overlapLeft < overlapRight;
        boolean ballFromTop = overlapTop < overlapBottom;

        double minOverlapX = ballFromLeft ? overlapLeft : overlapRight;
        double minOverlapY = ballFromTop ? overlapTop : overlapBottom;

        if (minOverlapX < minOverlapY) {
            mBall.velocityX = ballFromLeft ? -BALL_VELOCITY : BALL_VELOCITY;
        } else {
            mBall.velocityY = ballFromTop ? -BALL_VELOCITY : BALL_VELOCITY;
        }
    }

    public static void initializeBricks(List<Brick> bricks) {//инициализируем кирпичи

        bricks.clear();// убрать старые кирпичи

        for (int iX = 0; iX < COUNT_BLOCKS_X; ++iX) {
            for (int iY = 0; iY < COUNT_BLOCKS_Y; ++iY) {
                bricks.add(new Brick((iX + 1) * (BLOCK_WIDTH + 3) + 22,
                        (iY + 2) * (BLOCK_HEIGHT + 3) + 40));
            }
        }
    }

    public void run(List<Brick> bricks) {
        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = bf.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        boolean running = true;

        while (running) {

            long time1 = System.currentTimeMillis();

            if (!scoreboard.gameOver && !scoreboard.win) {
                tryAgain = false;
                update();
                drawScene(ball, bricks, scoreboard);



            } else {
                if (tryAgain) {
                    tryAgain = false;
                    initializeBricks(bricks);
                    scoreboard.lives = PLAYER_LIVES;
                    scoreboard.score = 0;
                    scoreboard.win = false;
                    scoreboard.gameOver = false;
                    scoreboard.updateScoreboard();
                    ball.x = SCREEN_WIDTH / 2;
                    ball.y = SCREEN_HEIGHT / 2;
                    paddle.x = SCREEN_WIDTH / 2;
                }
            }


            long time2 = System.currentTimeMillis();

            lastFt = (double) (time2 - time1);


        }

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }
    //обновление
    private void update() {

        currentSlide += lastFt;

        for (; currentSlide >= FT_SLICE; currentSlide -= FT_SLICE) { //...

            ball.update(scoreboard, paddle); //вызов метода обновления шара
            paddle.update(); //вызов метода обновления платформы
            testCollision(paddle, ball); //вызов метода проверки на столкновения

            Iterator<Brick> it = bricks.iterator();
            while (it.hasNext()) { //пока есть следующий элемент
                Brick brick = it.next(); //получаем следующий "кирпич"
                testCollision(brick, ball); //вызов метода проверки на столкновения
                if (brick.destroyed) { //если "кирпич" разрушается
                    it.remove(); //вызов метода удаления текущего элемента
                }
            }

        }
    }

    // код отрисовки сцены.
    private void drawScene(Ball ball, List<Brick> bricks, ScoreBoard scoreboard) {

        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = null;

        try {

            g = bf.getDrawGraphics();
            //g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
            Image img = new ImageIcon("image/background.jpg").getImage(); //загрузка изображения
            g.drawImage(img, 0, 0, null); //отображение

            ball.draw(g); //вызов метода отрисовки мяча
            paddle.draw(g); //вызов метода отрисовки платформы
            for (Brick brick : bricks) {
                brick.draw(g);
            }
            scoreboard.draw(g); //вызов метода отображения (отрисовки) счета

        }

        finally {
            g.dispose();
        }

        bf.show(); //процесс внутри окна обновляется
        Toolkit.getDefaultToolkit().sync(); //инструментарий

    }

    @Override
    public void keyPressed(KeyEvent event) {

        switch (event.getKeyCode()) {

            case KeyEvent.VK_ENTER:
                tryAgain = true;
                break;
            case KeyEvent.VK_LEFT:
                paddle.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                paddle.moveRight();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                paddle.stopMove();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }

}