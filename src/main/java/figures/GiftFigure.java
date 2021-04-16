package figures;

import combinatedFields.Acceleration;
import combinatedFields.SpriteDirection;
import imageFoundation.ImageObject;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logic.Gifts;

public class GiftFigure extends BaseFigure {
    public Image[] slicedGiftFigures;
    public int sheetSourceCols;
    public Acceleration acceleration;
    public Gifts gifts;

    public GiftFigure() {
        loadAndSliceGiftSpriteSheet("all-gifts.png");
        setDirection(SpriteDirection.RIGHT);
        acceleration = new Acceleration(50, 50);
    }

    public Image getImageForGifts() {
        return switch (gifts) {
            case ammo -> slicedGiftFigures[0];
            case chest -> slicedGiftFigures[1];
            case diamond -> slicedGiftFigures[2];
            case gift -> slicedGiftFigures[3];
            case grenade -> slicedGiftFigures[4];
            case gun -> slicedGiftFigures[5];
            case princess -> slicedGiftFigures[6];
            case question -> slicedGiftFigures[7];
        };
    }

    public Gifts getGifts() {
        return gifts;
    }

    public void setGifts(Gifts gifts) {
        this.gifts = gifts;
    }

    private void loadAndSliceGiftSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 8;
        setSpriteWidth(64); // 96 / 4
        setSpriteHeight(64); //
        slicedGiftFigures = new Image[sheetSourceCols];
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
                slicedGiftFigures[colStep] = writableImage;
            }
        }
    }
}
