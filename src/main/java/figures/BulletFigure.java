package figures;

import combinatedFields.Acceleration;
import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class BulletFigure  extends BaseFigure {
    public Image[] slicedBulletFigures;
    public int sheetSourceCols;
    //public int spriteWidth;
    //public int spriteHeight;
    public Acceleration acceleration;

    public BulletFigure() {
        loadAndSliceZombieSpriteSheet("bullets-96x24.png");
        setDirection(SpriteDirection.RIGHT);
        acceleration = new Acceleration(50, 50);
    }

    public Image getImageForDirection() {
        return switch (getDirection()) {
            case DOWN -> slicedBulletFigures[2];
            case UP -> slicedBulletFigures[0];
            case LEFT -> slicedBulletFigures[3];
            default -> slicedBulletFigures[1];
        };
    }

    private void loadAndSliceZombieSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 4;
        setSpriteWidth(24); // 96 / 4
        setSpriteHeight(24); //
        slicedBulletFigures = new Image[sheetSourceCols];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter;

            for (int colStep = 0; colStep < sheetSourceCols; colStep++) {
                WritableImage writableImage = new WritableImage(getSpriteWidth(),getSpriteHeight());
                pixelWriter = writableImage.getPixelWriter();

                for (int readY = 0; readY < getSpriteHeight(); readY++) {
                    for (int readX = 0; readX < getSpriteWidth(); readX++) {
                        Color color = pixelReader.getColor(readX + (colStep * (getSpriteWidth())), readY);
                        pixelWriter.setColor(readX, readY, color);
                    }
                }
                // the image is here, transfer writeable image to image
                slicedBulletFigures[colStep] = writableImage;
            }
        }
    }
/*
    public Position getCenterPositionInPixels() {
        return new Position(getPosition().getxPos() + spriteWidth / 2, getPosition().getyPos() + spriteHeight / 2);
    }
*/

}
