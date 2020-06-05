package com.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

public class Arkanoid extends JFrame implements KeyListener {

    private static final String FONT = "Courier New";

    private boolean tryAgain = false;

    private final Paddle paddle = new Paddle(Const.SCREEN_WIDTH / 2, Const.SCREEN_HEIGHT - 50); // начальное положение платформы
    private final Ball ball = new Ball(Const.SCREEN_WIDTH / 2, Const.SCREEN_HEIGHT / 2); //начальное положения мяча
    private static final List<Brick> bricks = new ArrayList<>();
    private final ScoreBoard scoreboard = new ScoreBoard(FONT);

    private double lastFt;
    private double currentSlide; //число текущего слайда

    public Arkanoid() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //запрещает изменять размер окна
        setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT); //размер окна
        setVisible(true); //видимость
        addKeyListener(this); //ключевой слушатель
        setLocationRelativeTo(null); //расположение по центру
        setTitle("Arkanoid"); //Заголовок

        createBufferStrategy(2); //БуфферСтратегия обновления окна

        initializeBricks(bricks); //инициализация "кирпичей"

    }


    public static void main(String[] args) {

        new Arkanoid().run();
    }

    public boolean isIntersecting(GameObject mA, GameObject mB) {
        return !(mA.right() >= mB.left()) || !(mA.left() <= mB.right())
                || !(mA.bottom() >= mB.top()) || !(mA.top() <= mB.bottom());
    }
    //Проверка на столкновение платформа - шар
    private void testCollision(Paddle mPaddle, Ball mBall) {
        if (isIntersecting(mPaddle, mBall)) //если платфома и шар пересекаются
            return;
        mBall.velocityY = -Const.BALL_VELOCITY;
        if (mBall.x < mPaddle.x)
            mBall.velocityX = -Const.BALL_VELOCITY;
        else
            mBall.velocityX = Const.BALL_VELOCITY;
    }
    // Проверка на столкновение для "кирпич" - шар
    private void testCollision(Brick mBrick, Ball mBall) {
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
            mBall.velocityX = ballFromLeft ? -Const.BALL_VELOCITY : Const.BALL_VELOCITY;
        } else {
            mBall.velocityY = ballFromTop ? -Const.BALL_VELOCITY : Const.BALL_VELOCITY;
        }
    }
    //инициализируем кирпичи
    private static void initializeBricks(List<Brick> bricks) {
        // убрать старые кирпичи
        bricks.clear();

        for (int iX = 0; iX < Const.COUNT_BLOCKS_X; ++iX) {
            for (int iY = 0; iY < Const.COUNT_BLOCKS_Y; ++iY) {
                bricks.add(new Brick((iX + 1) * (Const.BLOCK_WIDTH + 3) + 22,
                        (iY + 2) * (Const.BLOCK_HEIGHT + 3) + 40));
            }
        }
    }

    private void run() {
        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = bf.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        //boolean running = true;

        while (true) {

            long time1 = System.currentTimeMillis();

            if (!scoreboard.gameOver && !scoreboard.win) {
                tryAgain = false;
                update();
                drawScene(ball, scoreboard);



            } else {
                if (tryAgain) {
                    tryAgain = false;
                    initializeBricks(Arkanoid.bricks);
                    scoreboard.lives = Const.PLAYER_LIVES;
                    scoreboard.score = 0;
                    scoreboard.win = false;
                    scoreboard.gameOver = false;
                    scoreboard.updateScoreboard();
                    ball.x = Const.SCREEN_WIDTH / 2;
                    ball.y = Const.SCREEN_HEIGHT / 2;
                    paddle.x = Const.SCREEN_WIDTH / 2;
                }
            }


            long time2 = System.currentTimeMillis();

            lastFt = (double) (time2 - time1);


        }

        //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }
    //обновление
    private void update() {

        currentSlide += lastFt;

        for (; currentSlide >= Const.FT_SLICE; currentSlide -= Const.FT_SLICE) { //...

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
    private void drawScene(Ball ball, ScoreBoard scoreboard) {

        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = null;

        try {

            g = bf.getDrawGraphics();
            g.fillRect(0, 0, getWidth(), getHeight());
            Image img = new ImageIcon("image/background.jpg").getImage(); //загрузка изображения
            g.drawImage(img, 0, 0, null); //отображение

            ball.draw(g); //вызов метода отрисовки мяча
            paddle.draw(g); //вызов метода отрисовки платформы
            for (Brick brick : Arkanoid.bricks) {
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