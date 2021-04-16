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
import setups.IQ;

public class ZombieFigure extends BaseFigure {

    public final int MAX_STEPS = 4;
    private int currentStep = 0;
    public Image[][] slicedZombieFigures;
    public int sheetSourceCols;
    public int sheetSourceRows;
    /*
    public int spriteWidth;
    public int spriteHeight;

     */
    public Acceleration acceleration;
    private long timeDividerForNextImage;
    private int freezeCounter;
    private IQ iq;


    public ZombieFigure() {
        loadAndSliceZombieSpriteSheet("zombie.png");
        setDirection(SpriteDirection.RIGHT);
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

    public boolean isPositionInsideZombieFigure(Position positionInPixels) {
        int testPosX = positionInPixels.getxPos();
        int testPosY = positionInPixels.getyPos();
        int xPos = getPosition().getxPos();
        int yPos = getPosition().getyPos();
        return (testPosX >= xPos) && (testPosX <= xPos + getSpriteWidth()) && //
                (testPosY >= yPos) && (testPosY <= yPos + getSpriteHeight());
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
        return switch (getDirection()) {
            case DOWN -> slicedZombieFigures[currentStep][0];
            case UP -> slicedZombieFigures[currentStep][3];
            case LEFT -> slicedZombieFigures[currentStep][1];
            default -> slicedZombieFigures[currentStep][2];
        };
    }


    private void loadAndSliceZombieSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 4;
        sheetSourceRows = 4;
        setSpriteWidth( 32); // 128 / 4
        setSpriteHeight(48); // 192 / 4
        slicedZombieFigures = new Image[sheetSourceCols][sheetSourceRows];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter = null;
            for (int rowDirection = 0; rowDirection < sheetSourceRows; rowDirection++) {
                for (int colStep = 0; colStep < sheetSourceCols; colStep++) {
                    WritableImage writableImage = new WritableImage(getSpriteWidth(),getSpriteHeight());
                    pixelWriter = writableImage.getPixelWriter();

                    for (int readY = 0; readY < getSpriteHeight(); readY++) {
                        for (int readX = 0; readX < getSpriteWidth(); readX++) {
                            Color color = pixelReader.getColor(readX + (colStep * (getSpriteWidth())), readY + (rowDirection * (getSpriteHeight())));
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
        SpriteDirection newDirection = getDirection();
        double random = Math.random();
        if (random < 0.5001) {
            //System.out.println("Random 1");
            switch (getDirection()) {
                case UP -> newDirection = SpriteDirection.RIGHT;
                case RIGHT -> newDirection = SpriteDirection.DOWN;
                case DOWN -> newDirection = SpriteDirection.LEFT;
                case LEFT -> newDirection = SpriteDirection.UP;
            }
        } else {
            //System.out.println("Random 2");
            switch (getDirection()) {
                case UP -> newDirection = SpriteDirection.LEFT;
                case RIGHT -> newDirection = SpriteDirection.UP;
                case DOWN -> newDirection = SpriteDirection.RIGHT;
                case LEFT -> newDirection = SpriteDirection.DOWN;
            }
        }

        setDirection(newDirection);
    }

    public boolean mayChangeDirection() {
        return (((getDirection() == SpriteDirection.LEFT || getDirection() == SpriteDirection.RIGHT) && getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0)
                ||
                ((getDirection() == SpriteDirection.DOWN || getDirection()== SpriteDirection.UP) && getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0));
    }

    public void walk() {
        int x = getPosition().getxPos();
        int y = getPosition().getyPos();
        switch (getDirection()) {
            case LEFT -> x--;
            case RIGHT -> x++;
            case UP -> y--;
            case DOWN -> y++;
        }
        setPosition(new Position(x, y));
    }

    public void alignToTiles() {
        int x = getPosition().getxPos();
        int y = getPosition().getyPos();
        x = x - (getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        y = y - (getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        setPosition(new Position(x, y));
    }
}