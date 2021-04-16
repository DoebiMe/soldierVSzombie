package scoreEngine;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import figures.BulletFigure;
import figures.MainFigure;
import figures.ZombieFigure;
import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ScoreEngine {
    private static int lives;
    private static int kills;
    private static int level;
    private static int remainingBullets;
    private static Image imgBullets;
    private static Image imgLives;
    private static Image imgKills;
    private static Image imgLevels;
    private static Image[] imgNumbers;

    public static void setUp() {
        level = 1;
        kills = 0;
        lives = 3;
        remainingBullets = 100;
        ZombieFigure zombieFigure = new ZombieFigure();
        zombieFigure.setPosition(new Position(0, 0));
        zombieFigure.setDirection(SpriteDirection.DOWN);
        imgKills = zombieFigure.getNextImageForDirection();
        MainFigure mainFigure = new MainFigure();
        mainFigure.setPosition(new Position(0, 0));
        mainFigure.setDirection(SpriteDirection.RIGHT);
        imgLives = mainFigure.getImageForCurrentStepAndDirection();
        BulletFigure bulletFigure = new BulletFigure();
        bulletFigure.setDirection(SpriteDirection.UP);
        imgBullets = bulletFigure.getImageForDirection();
        loadAndSliceNumbersSpriteSheet("numbers0-9.png");
    }

    private static void loadAndSliceNumbersSpriteSheet(String spriteSheetSourceName) {
        int sheetSourceCols = 10;
        int sheetSourceRows = 1;
        int spriteWidth = 1096 / 10; // 128 / 4
        int spriteHeight = 82; // 192 / 4
        imgNumbers = new Image[10];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter = null;
            for (int rowDirection = 0; rowDirection < sheetSourceRows; rowDirection++) {
                for (int colStep = 0; colStep < sheetSourceCols; colStep++) {
                    WritableImage writableImage = new WritableImage(spriteWidth, spriteHeight);
                    pixelWriter = writableImage.getPixelWriter();

                    for (int readY = 0; readY < spriteHeight; readY++) {
                        for (int readX = 0; readX < spriteWidth; readX++) {
                            Color color = pixelReader.getColor(readX + (colStep * (spriteWidth + 0)), readY + (rowDirection * (spriteHeight + 0)));
                            pixelWriter.setColor(readX, readY, color);
                        }
                    }
                    // the image is here, transfer writeable image to image
                    imgNumbers[colStep + rowDirection * sheetSourceCols] = writableImage;
                }

            }
        }
    }

    public static int getRemainingBullets() {
        return remainingBullets;
    }

    public static void setRemainingBullets(int remainingBullets) {
        ScoreEngine.remainingBullets = remainingBullets;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        ScoreEngine.lives = lives;
    }


    public static int getKills() {
        return kills;
    }

    public static void setKills(int kills) {
        ScoreEngine.kills = kills;
    }

    public static void addToKills(int numberToAdd) {
        kills += numberToAdd;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        ScoreEngine.level = level;
    }

    public static Image getImgLives() {
        return imgLives;
    }

    public static Image getImgKills() {
        return imgKills;
    }

    public static Image getImgLevels() {
        return imgLevels;
    }

    public static Image getImgBullets() { return imgBullets;  }

    public static Image getImgNumber(int number) {
        return imgNumbers[number];
    }

}
