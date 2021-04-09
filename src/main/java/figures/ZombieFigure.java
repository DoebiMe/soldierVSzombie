package figures;

import combinatedFields.Acceleration;
import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;
import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logic.GeneralGameLogic;
import setups.IQ;

import java.util.Random;

import static spriteFoundation.BackGround.getyPos;

public class ZombieFigure implements Figure {

    public final int MAX_STEPS = 4;
    private int currentStep = 0;
    private SpriteDirection spriteDirection;
    public Image[][] slicedZombieFigures;
    public int sheetSourceCols;
    public int sheetSourceRows;
    public int spriteWidth;
    public int spriteHeight;
    public Acceleration acceleration;
    private Position position;
    private long timeDividerForNextImage;
    private int freezeCounter;
    private IQ iq;


    public ZombieFigure() {
        loadAndSliceZombieSpriteSheet("zombie.png");
        spriteDirection = SpriteDirection.RIGHT;
        currentStep = 0;
        acceleration = new Acceleration(50, 50);
        timeDividerForNextImage = 0;
        freezeCounter = 0;
        iq = IQ.stupid;
    }

    public boolean isInFreeze() {
        return freezeCounter != 0;
    }

    public void setFreezeCounter(int freezeCounter) {
        if (freezeCounter < 0) {
            freezeCounter = 0;
        }
        this.freezeCounter = freezeCounter;
    }

    public void decrementFreezeCounter() {
        if (freezeCounter > 0) {
            freezeCounter--;
        }

    }

    public IQ getIq() {
        return iq;
    }

    public void setIq(IQ iq) {
        this.iq = iq;
    }


    public SpriteDirection getSpriteDirection() {
        return spriteDirection;
    }

    public void setSpriteDirection(SpriteDirection spriteDirection) {
        this.spriteDirection = spriteDirection;
    }

    public Image getNextImageForDirection() {
        timeDividerForNextImage--;
        if (timeDividerForNextImage <= 0) {
            currentStep++;
            if (currentStep > MAX_STEPS - 1) {
                currentStep = 0;
            }
            timeDividerForNextImage = 7;
        }
        return switch (spriteDirection) {
            case DOWN -> slicedZombieFigures[currentStep][0];
            case UP -> slicedZombieFigures[currentStep][3];
            case LEFT -> slicedZombieFigures[currentStep][1];
            default -> slicedZombieFigures[currentStep][2];
        };
    }


    private void loadAndSliceZombieSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 4;
        sheetSourceRows = 4;
        spriteWidth = 32; // 128 / 4
        spriteHeight = 48; // 192 / 4
        slicedZombieFigures = new Image[sheetSourceCols][sheetSourceRows];
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
                    slicedZombieFigures[colStep][rowDirection] = writableImage;
                }

            }
        }
    }


    public void setNextDirection() {
        SpriteDirection newDirection = spriteDirection;
        double random = Math.random();
        if (random < 0.5001) {
            //System.out.println("Random 1");
            switch (spriteDirection) {
                case UP -> newDirection = SpriteDirection.RIGHT;
                case RIGHT -> newDirection = SpriteDirection.DOWN;
                case DOWN -> newDirection = SpriteDirection.LEFT;
                case LEFT -> newDirection = SpriteDirection.UP;
            }
        } else {
            //System.out.println("Random 2");
            switch (spriteDirection) {
                case UP -> newDirection = SpriteDirection.LEFT;
                case RIGHT -> newDirection = SpriteDirection.UP;
                case DOWN -> newDirection = SpriteDirection.RIGHT;
                case LEFT -> newDirection = SpriteDirection.DOWN;
            }
        }

        spriteDirection = newDirection;
    }

    public void lineUpToTiles() {
        Position currentPosition = position;
        switch (spriteDirection) {
            case RIGHT, LEFT -> setPosition(new Position(currentPosition.getxPos(), currentPosition.getyPos() / DrawEngine.SCALE_FACTOR_SPRITE * DrawEngine.SCALE_FACTOR_SPRITE));
            case DOWN, UP -> setPosition(new Position(currentPosition.getxPos() / DrawEngine.SCALE_FACTOR_SPRITE * DrawEngine.SCALE_FACTOR_SPRITE, currentPosition.getyPos()));

        }
    }

    public Position getPositionScaledToTiles() {
        Position positionToScale = new Position(position.getxPos() / DrawEngine.SCALE_FACTOR_SPRITE, position.getyPos() / DrawEngine.SCALE_FACTOR_SPRITE);

        if (spriteDirection == SpriteDirection.LEFT) {
            positionToScale.setxPos(positionToScale.getxPos() + 1);
        }
        if (spriteDirection == SpriteDirection.UP) {
            positionToScale.setyPos(positionToScale.getyPos() + 1);
        }
        return positionToScale;
    }

    public boolean mayChangeDirection() {
        return  (((spriteDirection == SpriteDirection.LEFT || spriteDirection == SpriteDirection.RIGHT) && position.getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0)
                ||
                ((spriteDirection == SpriteDirection.DOWN || spriteDirection == SpriteDirection.UP) && position.getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0));
    }

    public void walk() {
        int x = position.getxPos();
        int y = position.getyPos();
        switch (getSpriteDirection()) {
            case LEFT -> x--;
            case RIGHT -> x++;
            case UP -> y--;
            case DOWN -> y++;
        }
        setPosition(new Position(x, y));
    }

    public void allignToTiles() {
        int x = position.getxPos();
        int y = position.getyPos();
        x = x - (position.getxPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        y = y - (position.getyPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        setPosition(new Position(x, y));
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Position getPositionInTiles() {
        return DrawEngine.getPixelsTranslatedToTiles(position);
    }

    @Override
    public Position getPositionInPixels() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public SpriteDirection getDirection() {
        return spriteDirection;
    }

    @Override
    public void setDirection(SpriteDirection newDirection) {
        this.spriteDirection = newDirection;
        int x = position.getxPos();
        int y = position.getyPos();
        switch (spriteDirection) {
            case LEFT, RIGHT -> x -=  (x % DrawEngine.SCALE_FACTOR_SPRITE);
            case UP, DOWN -> y -=  (y % DrawEngine.SCALE_FACTOR_SPRITE);
        }
        position = new Position(x,y);
    }
    public boolean isPositionAllignedWithTiles() {
        boolean result =  position.getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0 &&
                position.getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0;
        System.out.println(" isPositionAllignedWithTiles == " + result);
        return result;
    }
}
