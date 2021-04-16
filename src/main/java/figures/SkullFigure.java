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

public class SkullFigure extends BaseFigure {

    public final int MAX_STEPS = 8;
    private int currentStep = 0;
    public Image[] slicedSkullFigures;
    public int spriteWidth;
    public int spriteHeight;
    public Acceleration acceleration;
    private long timeDividerForNextImage;
    private long timeDividerForNextPosition;
    private final long IDNr;
    private static long IDNrCount=0;
    public final long TIME_DIVIDER_POSTION = 1;
    public final long FREEZE_TIME = 500;


    public SkullFigure() {
        IDNrCount++;
        IDNr = IDNrCount;
        loadAndSliceSkullFigureSpriteSheet("skull-sheet.png");
        setDirection(SpriteDirection.RIGHT);
        currentStep = 0;
        acceleration = new Acceleration(50, 50);
        timeDividerForNextImage = 0;
        timeDividerForNextPosition =0;
    }

    public Image getNextImage() {
        timeDividerForNextImage--;
        if (timeDividerForNextImage <= 0) {
            currentStep++;
            if (currentStep > MAX_STEPS - 1) {
                currentStep = 0;
            }
            timeDividerForNextImage = 7;
        }
        return slicedSkullFigures[currentStep];
    }

    public boolean isTimeForNextPosition() {
        timeDividerForNextPosition--;
        if (timeDividerForNextPosition < 0) {
            timeDividerForNextPosition = TIME_DIVIDER_POSTION;
            return true;
        }
        return false;
    }


    public void freeze() {
        timeDividerForNextPosition = FREEZE_TIME;
    }


    private void loadAndSliceSkullFigureSpriteSheet(String spriteSheetSourceName) {
        //3200 x400
        spriteWidth = 400; // 3200 / 8
        spriteHeight = 400; // 400 / 1
        slicedSkullFigures = new Image[MAX_STEPS];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter = null;

                for (int colStep = 0; colStep < MAX_STEPS; colStep++) {
                    WritableImage writableImage = new WritableImage(spriteWidth, spriteHeight);
                    pixelWriter = writableImage.getPixelWriter();

                    for (int readY = 0; readY < spriteHeight; readY++) {
                        for (int readX = 0; readX < spriteWidth; readX++) {
                            Color color = pixelReader.getColor(readX + (colStep * (spriteWidth)), readY );
                            pixelWriter.setColor(readX, readY, color);
                        }
                    }
                    // the image is here, transfer writeable image to image
                    slicedSkullFigures[colStep] = writableImage;
                }
        }
    }

    public long getIDNr() {
        return IDNr;
    }

    public boolean isInFreeze() {
        return  (timeDividerForNextPosition > TIME_DIVIDER_POSTION);
    }
}