package drawEngine;


import combinatedFields.Position;
import figures.*;
import javafx.scene.paint.Color;
import scoreEngine.ScoreEngine;
import setups.GraphicSetup;
import setups.TilesSetup;


public class DrawEngine {

    public static final int SCALE_FACTOR_SPRITE = 60;

    public static final int absoluteXOffset = 0;
    public static final int absoluteYOffset = 100;


    public static int getSubX(MainFigure mainFigure) {
        int subX = 0;
        if (mainFigure.getPosition().getxPos() < 700) {
            subX = mainFigure.getPosition().getxPos() - 700;
        }
        return subX + absoluteXOffset;
    }

    public static int getSubY(MainFigure mainFigure) {
        int subY = 0;

        if (mainFigure.getPosition().getyPos() < 400) {
            subY = mainFigure.getPosition().getyPos() - 400;
        }
        return subY + absoluteYOffset;
    }


    public static void drawMainFigure(MainFigure mainFigure) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);

        GraphicSetup.getGraphicsContext().drawImage(
                mainFigure.getImageForCurrentStepAndDirection(),//
                GraphicSetup.getHalfWidth() + subX,
                GraphicSetup.getHalfHeight() + subY,
                SCALE_FACTOR_SPRITE,
                SCALE_FACTOR_SPRITE);
    }

    public static void drawAllTiles(TilesSetup tilesSetup, MainFigure mainFigure, boolean completeRedraw) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);

        GraphicSetup.getGraphicsContext().setFill(Color.GREY);
        if (completeRedraw) {
            GraphicSetup.getGraphicsContext().fillRect(0, 0, GraphicSetup.getWidth(), GraphicSetup.getHeight());
        } else {
            GraphicSetup.getGraphicsContext().fillRect(0, 0, GraphicSetup.getWidth(), GraphicSetup.getHeight());
        }
        for (int row = 0; row < TilesSetup.TILE_MAP_ROWS; row++) {
            int startY = (GraphicSetup.getHalfHeight() + (row * SCALE_FACTOR_SPRITE) - mainFigure.getPosition().getyPos());
            startY += subY;

            if ((startY < -SCALE_FACTOR_SPRITE) || //
                    (startY > GraphicSetup.getHeight())) {
                continue;
            }
            for (int col = 0; col < TilesSetup.TILE_MAP_COLS; col++) {
                int id = tilesSetup.tileMapId[col][row];
                if (id != 0) {
                    id--;
                    int startX = (GraphicSetup.getHalfWidth() + (col * SCALE_FACTOR_SPRITE) - mainFigure.getPosition().getxPos());
                    startX += subX;
                    if ((startX < -SCALE_FACTOR_SPRITE) || //
                            (startX > GraphicSetup.getWidth())) {
                        continue;
                    }

                    GraphicSetup.getGraphicsContext().drawImage(tilesSetup.slicedTiles[id],
                            startX, startY, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
                    );
                }
            }
        }
    }

    public static void drawAllZombies(MainFigure mainFigure, ZombieCollection zombieCollection) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            Position position = zombieFigure.getPosition();
            int x = GraphicSetup.getHalfWidth() + position.getxPos() - mainFigure.getPosition().getxPos() + subX;
            int y = GraphicSetup.getHalfHeight() + position.getyPos() - mainFigure.getPosition().getyPos() + subY;

            GraphicSetup.getGraphicsContext().drawImage(zombieFigure.getNextImageForDirection(),
                    x, y,
                    SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
            );
        }
    }

    public static void drawAllSkulls(MainFigure mainFigure, SkullCollection skullCollection) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);
        for (SkullFigure skullFigure : skullCollection.skullFigureList) {
            Position position = skullFigure.getPosition();
            int x = GraphicSetup.getHalfWidth() + position.getxPos() - mainFigure.getPosition().getxPos() + subX;
            int y = GraphicSetup.getHalfHeight() + position.getyPos() - mainFigure.getPosition().getyPos() + subY;

            GraphicSetup.getGraphicsContext().drawImage(skullFigure.getNextImage(),
                    x, y,
                    SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
            );
        }
    }
    public static void drawAllGifts(MainFigure mainFigure, GiftCollection giftCollection) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);
        for (GiftFigure  giftFigure : giftCollection.giftFigureList) {
            Position position = giftFigure.getPosition();
            int x = GraphicSetup.getHalfWidth() + position.getxPos() - mainFigure.getPosition().getxPos() + subX;
            int y = GraphicSetup.getHalfHeight() + position.getyPos() - mainFigure.getPosition().getyPos() + subY;

            GraphicSetup.getGraphicsContext().drawImage(giftFigure.getImageForGifts(),
                    x, y,
                    SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
            );
        }
    }



    public static void drawScoreBoard() {
        GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgLives(), 10, 10, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE);
        GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgKills(), 200, 10, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE);
        GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgBullets(),400,10,SCALE_FACTOR_SPRITE,SCALE_FACTOR_SPRITE);

        String killsAsString = Integer.toString(ScoreEngine.getKills());
        for (int lus = 0; lus < killsAsString.length(); lus++) {
            int number = Integer.valueOf(killsAsString.charAt(lus) - 48);
            GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgNumber(number), 290 + 40 * lus, 10, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE);
        }
        String livesAsString = Integer.toString(ScoreEngine.getLives());
        for (int lus = 0; lus < livesAsString.length(); lus++) {
            int number = Integer.valueOf(livesAsString.charAt(lus) - 48);
            GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgNumber(number), 90 + 40 * lus, 10, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE);
        }
        String remainingBulletsAsString = Integer.toString(ScoreEngine.getRemainingBullets());
        for (int lus = 0; lus < remainingBulletsAsString.length(); lus++) {
            int number = Integer.valueOf(remainingBulletsAsString.charAt(lus) - 48);
            GraphicSetup.getGraphicsContext().drawImage(ScoreEngine.getImgNumber(number), 490 + 40 * lus, 10, SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE);
        }

    }

    public static void drawAllBullets(MainFigure mainFigure, BulletCollection bulletCollection) {
        int subX = getSubX(mainFigure);
        int subY = getSubY(mainFigure);
        for (BulletFigure bulletFigure : bulletCollection.bulletFigureList) {
            Position position = bulletFigure.getPosition();
            int x = GraphicSetup.getHalfWidth() + position.getxPos() - mainFigure.getPosition().getxPos() + subX;
            int y = GraphicSetup.getHalfHeight() + position.getyPos() - mainFigure.getPosition().getyPos() + subY;

            GraphicSetup.getGraphicsContext().drawImage(bulletFigure.getImageForDirection(),
                    x, y,
                    SCALE_FACTOR_SPRITE, SCALE_FACTOR_SPRITE
            );
        }
    }

/*
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
 */
    /*

    private static Image rotateImage(Image image, long rotation) {
        ImageView iv = new ImageView(image);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        params.setTransform(new Rotate(rotation, (image.getWidth()) / 2, (image.getHeight()) / 2));
        params.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));

        return iv.snapshot(params, null);
    }

     */

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
