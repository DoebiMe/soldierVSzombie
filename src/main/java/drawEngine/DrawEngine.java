package drawEngine;


import combinatedFields.Position;
import figures.MainFigure;
import figures.ZombieCollection;
import figures.ZombieFigure;
import imageFoundation.ImageFoundation;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import setups.BackgroundSetup;
import setups.GraphicSetup;
import setups.TilesSetup;
import spriteFoundation.BackGround;
import spriteFoundation.Sprite;
import spriteFoundation.SpriteCollection;


public class DrawEngine {

    public static final int SCALE_FACTOR_SPRITE = 60;


    public static void drawBackground() {
        GraphicSetup.getGraphicsContext().drawImage(BackGround.getImage(0), BackgroundSetup.getxPos(), BackgroundSetup.getYpos());

    }

    public static int getXSubtractor(MainFigure mainFigure) {
        int x = mainFigure.getPosition().getxPos();
        if (x < 200) {
            x -=200;
        }
        return x;
    }


    public static void drawMainFigure(MainFigure mainFigure) {
        GraphicSetup.getGraphicsContext().drawImage(
                mainFigure.getImageForCurrentStepAndDirection(),//
                GraphicSetup.getHalfWidth(),
                GraphicSetup.getHalfHeight(),
                SCALE_FACTOR_SPRITE,
                SCALE_FACTOR_SPRITE);
    }

    public static void drawAllTiles(TilesSetup tilesSetup, MainFigure mainFigure, boolean completeRedraw) {
        GraphicSetup.getGraphicsContext().setFill(Color.GREY);
        if (completeRedraw) {
            GraphicSetup.getGraphicsContext().fillRect(0, 0, GraphicSetup.getWidth(), GraphicSetup.getHeight());
        } else {
            GraphicSetup.getGraphicsContext().fillRect(0, 0, GraphicSetup.getWidth(), GraphicSetup.getHeight());
        }
        for (int row = 0; row < TilesSetup.TILE_MAP_ROWS; row++) {
            for (int col = 0; col < TilesSetup.TILE_MAP_COLS; col++) {
                int id = tilesSetup.tileMapId[col][row];
                if (id != 0) {
                    id--;
                    int startX = (GraphicSetup.getHalfWidth() + (col * SCALE_FACTOR_SPRITE) - mainFigure.getPosition().getxPos());
                    int startY = (GraphicSetup.getHalfHeight() + (row * SCALE_FACTOR_SPRITE) - mainFigure.getPosition().getyPos());

                    if ((startX < -SCALE_FACTOR_SPRITE) || //
                            (startY < -SCALE_FACTOR_SPRITE) || //
                            (startX > GraphicSetup.getWidth()) || //
                            (startY > GraphicSetup.getHeight())) {
                        continue;
                    }

                    GraphicSetup.getGraphicsContext().drawImage(tilesSetup.slicedTiles[id],
                            startX,
                            startY,
                            SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE

                    );
                }
            }
        }
    }

    public static void drawAllZombies(MainFigure mainFigure, ZombieCollection zombieCollection) {
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            Position position = zombieFigure.getPosition();
            int x = GraphicSetup.getHalfWidth() + position.getxPos() - mainFigure.getPosition().getxPos();
            int y = GraphicSetup.getHalfHeight() + position.getyPos() - mainFigure.getPosition().getyPos();

            GraphicSetup.getGraphicsContext().drawImage(zombieFigure.getNextImageForDirection(),
                    x, y,
                    SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
            );
        }

    }


    public static void drawZombieTarget(TilesSetup tilesSetup, long xOffset, long yOffset, long stepX, long stepY, Position positionToScale) {
        int id = tilesSetup.tileMapId[0][10];
        GraphicSetup.getGraphicsContext().drawImage(tilesSetup.slicedTiles[18 * 10 + 0],
                (stepX + (positionToScale.getxPos() - xOffset) * SCALE_FACTOR_SPRITE),
                (stepY + (positionToScale.getyPos() - yOffset) * SCALE_FACTOR_SPRITE),
                SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE

        );
    }


    public static void drawAllSprites() {

        for (Sprite sprite : SpriteCollection.getSpriteList()) {
            if (sprite.isEnabled() && sprite.isVisible()) {
                GraphicSetup.getGraphicsContext().drawImage(
                        sprite.getRotation() == 0
                                ? ImageFoundation.getImage(sprite.getImageId()) //
                                : rotateImage(ImageFoundation.getImage(sprite.getImageId()), sprite.getRotation()), //
                        sprite.getPosition().getxPos(), //
                        sprite.getPosition().getyPos());
            }
        }
    }

    private static Image rotateImage(Image image, long rotation) {
        ImageView iv = new ImageView(image);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        params.setTransform(new Rotate(rotation, (image.getWidth()) / 2, (image.getHeight()) / 2));
        params.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));

        return iv.snapshot(params, null);
    }

    public static Position getPixelsTranslatedToTiles(Position pixelPosition) {
        Position scaledPosition = new Position(
                (pixelPosition.getxPos() / SCALE_FACTOR_SPRITE), //+ TilesSetup.TILE_MAP_COLS /2,
                (pixelPosition.getyPos() / SCALE_FACTOR_SPRITE));// + (TilesSetup.TILE_MAP_ROWS /2) +1 ); // +1 cause row is odd
        return scaledPosition;
    }

    public static Position getTilesTranslatedToPixels(Position tilePosition) {
        Position scaledPosition = new Position(
                tilePosition.getxPos() * SCALE_FACTOR_SPRITE,
                tilePosition.getyPos() * SCALE_FACTOR_SPRITE + 1
        );
        return scaledPosition;
    }

}
