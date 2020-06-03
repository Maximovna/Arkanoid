package com.example;

import java.awt.*;

import static com.example.Arkanoid.*;


public class ScoreBoard {

    public int score = 0;
    public int lives = PLAYER_LIVES;
    public boolean win = false;
    public boolean gameOver = false;
    String text;
    Font font;

    ScoreBoard(String FONT) {
        font = new Font(FONT, Font.PLAIN, 12);
        text = "Welcome to Arkanoid Java version";
    }
    //увеличение счета
    public void increaseScore() {
        score++; //счет увеличивается
        if (score == (COUNT_BLOCKS_X * COUNT_BLOCKS_Y)) { //если счет заполняет весь блок
            win = true; //выигрыш
            text = "You have won! \nYour score was: " + score + "\n\nPress Enter to restart";
        } else {
            updateScoreboard();  // обновляем счет
        }
    }
    //мяч "умирает"
    public void die() {
        lives--; //жизни уменьшаются
        if (lives == 0) { //если жизни кончились
            gameOver = true; //проигрыш
            text = "You have lost! \n Your score was: " + score + "\n\n Press Enter to restart";
        } else {
            updateScoreboard(); //обновляем счет
        }
    }

    public void updateScoreboard() {

        text = "Score: " + score + "  Lives: " + lives;
    }
    //метод вывода текста блока "счет"
    public void draw(Graphics g) {
        if (win || gameOver) { //в случае, если выигрыш или проигрыш
            font = font.deriveFont(50f); //задать размер шрифта
            FontMetrics fontMetrics = g.getFontMetrics(font);
            g.setColor(Color.ORANGE); //цвет текста
            g.setFont(font); //шрифт
            int titleHeight = fontMetrics.getHeight(); //горизонтальные границы строки
            int lineNumber = 1; //номер линии
            for (String line : text.split("\n")) { //чтобы разделить текст на строки
                int titleLen = fontMetrics.stringWidth(line); //вертикальные границы строки
                g.drawString(line, (SCREEN_WIDTH / 2) - (titleLen / 2),
                        (SCREEN_HEIGHT / 4) + (titleHeight * lineNumber)); //определение положения строки
                lineNumber++; //увеличивается номер строки

            }
        } else { //в процессе игры
            font = font.deriveFont(34f); //задать размер шрифта
            FontMetrics fontMetrics = g.getFontMetrics(font);
            g.setColor(Color.LIGHT_GRAY); //цвет текста
            g.setFont(font); //шрифт
            int titleLen = fontMetrics.stringWidth(text); //вертикальные границы строки
            int titleHeight = fontMetrics.getHeight(); //горизонтальные границы строки
            g.drawString(text, (SCREEN_WIDTH / 2) - (titleLen / 2), titleHeight + 20); //определение положеня строки

        }
    }

}

