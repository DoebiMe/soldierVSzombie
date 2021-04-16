package figures;

import combinatedFields.*;
import drawEngine.DrawEngine;
import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MainFigure extends BaseFigure {

    public final int MAX_STEPS = 7;
    private int currentStep = 0;
    public Image[][] slicedMainFigures;
    public int sheetSourceCols;
    public int sheetSourceRows;
    public final int WALK_SIZE = 4;     // must be a divider of DrawEngine.SCALE_FACTOR

    public MainFigure() {

        mainFigureSetup();
    }

    public void mainFigureSetup() {

        loadAndSliceMainSpriteSheet("top down soldier.png");
        setPosition(new Position(DrawEngine.SCALE_FACTOR_SPRITE, DrawEngine.SCALE_FACTOR_SPRITE)); // = TILE 1,1
        setDirection(SpriteDirection.LEFT);
        currentStep = 0;
    }


    public int getXPosModuloScale() {
        return getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE;
    }

    public int getYPosModuloScale() {
        return getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE;
    }

    public void setNextStep() {
        currentStep++;
        if (currentStep > MAX_STEPS - 1) {
            currentStep = 0;
        }
    }

    public void walk(boolean keyMovementPressed, boolean canWalkThatDirection) {
        int x = 0;
        int y = 0;
        switch (getDirection()) {
            case UP -> {
                if ((keyMovementPressed && canWalkThatDirection) || (getYPosModuloScale() != 0)) {
                    y = -WALK_SIZE;
                }
            }
            case DOWN -> {
                if ((keyMovementPressed && canWalkThatDirection) || (getYPosModuloScale() != 0)) {
                    y = WALK_SIZE;
                }
            }
            case RIGHT -> {
                if ((keyMovementPressed && canWalkThatDirection) || (getXPosModuloScale() != 0)) {
                    x = WALK_SIZE;
                }
            }
            case LEFT -> {
                if ((keyMovementPressed && canWalkThatDirection) || (getXPosModuloScale() != 0)) {
                    x = -WALK_SIZE;
                }
            }
        }
        setPosition( new Position(getPosition().getxPos() + x, getPosition().getyPos() + y));
    }


    public Image getImageForCurrentStepAndDirection() {
        return switch (getDirection()) {
            case DOWN -> slicedMainFigures[currentStep][0];
            case UP -> slicedMainFigures[currentStep][1];
            case LEFT -> slicedMainFigures[currentStep][2];
            default -> slicedMainFigures[currentStep][3];
        };
    }

    private void loadAndSliceMainSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 7; // last 3 not used
        sheetSourceRows = 4;
        setSpriteWidth( 150); // 1650 / 11
        setSpriteHeight(117); // 468 / 4
        //Image[] mainFiguresAsRow = new Image[sheetSourceCols * sheetSourceRows];
        slicedMainFigures = new Image[sheetSourceCols][sheetSourceRows];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            final int LEFT_OFFSET = 30;
            final int TOP_OFFSET = 35;
            final int RIGHT_OFFSET = 50;
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter;
            for (int rowDirection = 0; rowDirection < sheetSourceRows; rowDirection++) {
                for (int colStep = 0; colStep < sheetSourceCols; colStep++) {
                    WritableImage writableImage = new WritableImage(getSpriteWidth() - LEFT_OFFSET - RIGHT_OFFSET, getSpriteHeight() - TOP_OFFSET);
                    pixelWriter = writableImage.getPixelWriter();

                    for (int readY = TOP_OFFSET; readY < getSpriteHeight(); readY++) {
                        for (int readX = LEFT_OFFSET; readX < getSpriteWidth() - RIGHT_OFFSET; readX++) {
                            Color color = pixelReader.getColor(readX + (colStep * (getSpriteWidth())), readY + (rowDirection * (getSpriteHeight())));
                            pixelWriter.setColor(readX - LEFT_OFFSET, readY - TOP_OFFSET, color);
                        }
                    }
                    // the image is here, transfer writeable image to image
                    slicedMainFigures[colStep][rowDirection] = writableImage;
                }
            }
        }
    }

    public void alignToTiles() {
        int x = getPosition().getxPos();
        int y = getPosition().getyPos();
        x = x - (getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        y = y - (getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE);
        setPosition(new Position(x, y));
    }

    @Override
    public void setDirection(SpriteDirection newSpriteDirection) {
        setDirectionUnAltered(newSpriteDirection);
    }
}