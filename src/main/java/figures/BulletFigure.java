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

public class BulletFigure implements Figure {
    private SpriteDirection spriteDirection;
    public Image[] slicedBulletFigures;
    public int sheetSourceCols;
    public int spriteWidth;
    public int spriteHeight;
    public Acceleration acceleration;
    private Position position;
    private long timeDividerForNextImage;

    public BulletFigure() {
        loadAndSliceZombieSpriteSheet("bullets-96x24.png");
        spriteDirection = SpriteDirection.RIGHT;
        acceleration = new Acceleration(50, 50);
        timeDividerForNextImage = 0;
    }

    public Image getImageForDirection() {
        return switch (spriteDirection) {
            case DOWN -> slicedBulletFigures[2];
            case UP -> slicedBulletFigures[0];
            case LEFT -> slicedBulletFigures[3];
            default -> slicedBulletFigures[1];
        };
    }

    private void loadAndSliceZombieSpriteSheet(String spriteSheetSourceName) {
        sheetSourceCols = 4;
        spriteWidth = 24; // 96 / 4
        spriteHeight = 24; //
        slicedBulletFigures = new Image[sheetSourceCols];
        ImageObject imageObject = new ImageObject(0, spriteSheetSourceName);
        if (imageObject.isLoadSuccess()) {
            Image sourceImage = imageObject.getImage();
            PixelReader pixelReader = sourceImage.getPixelReader();
            PixelWriter pixelWriter = null;

            for (int colStep = 0; colStep < sheetSourceCols; colStep++) {
                WritableImage writableImage = new WritableImage(spriteWidth, spriteHeight);
                pixelWriter = writableImage.getPixelWriter();

                for (int readY = 0; readY < spriteHeight; readY++) {
                    for (int readX = 0; readX < spriteWidth; readX++) {
                        Color color = pixelReader.getColor(readX + (colStep * (spriteWidth + 0)), readY);
                        pixelWriter.setColor(readX, readY, color);
                    }
                }
                // the image is here, transfer writeable image to image
                slicedBulletFigures[colStep] = writableImage;
            }
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

    public Position getCenterPositionInPixels() {
        return new Position(position.getxPos() + spriteWidth / 2, position.getyPos() + spriteHeight / 2);
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
            case LEFT, RIGHT -> x -= (x % DrawEngine.SCALE_FACTOR_SPRITE);
            case UP, DOWN -> y -= (y % DrawEngine.SCALE_FACTOR_SPRITE);
        }
        position = new Position(x, y);
    }

}
