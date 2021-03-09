package logic;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;
import figures.MainFigure;
import figures.ZombieCollection;
import figures.ZombieFigure;
import setups.KeyboardSetup;
import setups.TilesSetup;

import java.util.List;
import java.util.Random;

public class GeneralGameLogic {

    public static final List<Integer> canWalkOnIdList = List.of(0, 175, 181, 186, 187);

    private static StateOfGame stateOfGame;
    private static MainFigure mainFigure;
    private static TilesSetup tilesSetup;
    private static ZombieCollection zombieCollection;

    public GeneralGameLogic() {
        generalGameLogicSetup();
    }

    public static void generalGameLogicSetup() {
        tilesSetup = new TilesSetup();
        mainFigure = new MainFigure();
        stateOfGame = StateOfGame.play;
        zombieCollection = new ZombieCollection(tilesSetup);
    }

    public static StateOfGame getStateOfGame() {
        return stateOfGame;
    }

    public static void setStateOfGame(StateOfGame theStateOfGame) {
        stateOfGame = theStateOfGame;
    }


    public static void executeLogicTimeLine() {
        ProcessKeys.processPressedKeysGeneral();
        boolean keyMovementPressed = getIsKeyForMainSpritePressedAndHandleDirection();

        if (stateOfGame == StateOfGame.play) {
            mainFigure.setNextStep();
            mainFigure.walk(keyMovementPressed, canWalkThatDirection(mainFigure.getPosition(), mainFigure.getDirection()));

            boolean transporterMovement = isStandingOnTransporter(mainFigure.getPosition());
            if (transporterMovement) {
                Position newPositionInTiles = getNextTransporterPositionInTiles(
                        DrawEngine.getPixelsTranslatedToTiles(mainFigure.getPosition()), mainFigure.getDirection());
                Position newPositionInPixels = DrawEngine.getTilesTranslatedToPixels(newPositionInTiles);
                mainFigure.setPosition(newPositionInPixels);
                mainFigure.allignToTiles();
            }

            performZombiesWalking();
            performZombiesWalkingOnTransporter();
            performZombieFreezing();
            DrawEngine.drawAllTiles(tilesSetup, mainFigure,transporterMovement);
            DrawEngine.drawAllZombies(mainFigure,zombieCollection);
            DrawEngine.drawMainFigure(mainFigure);
        }
    }


    private static void performZombieFreezing() {
        Random random = new Random();
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            zombieFigure.decrementFreezeCounter();
            if ((zombieFigure.getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) &&
                    (zombieFigure.getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE ==0) &&
                    (!zombieFigure.isInFreeze())) {
                if (random.nextInt(2000)==1000) {
                    zombieFigure.setFreezeCounter(200);
                }
            }
        }
    }

    private static void performZombiesWalkingOnTransporter() {
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            if (isStandingOnTransporter(zombieFigure.getPosition())) {
                Position newPositionInTiles = getNextTransporterPositionInTiles(
                        DrawEngine.getPixelsTranslatedToTiles(zombieFigure.getPosition()), zombieFigure.getDirection());
                Position newPositionInPixels = DrawEngine.getTilesTranslatedToPixels(newPositionInTiles);
                zombieFigure.setPosition(newPositionInPixels);
                zombieFigure.allignToTiles();
            }
        }
    }

    private static boolean isStandingOnTransporter(Position positionInPixels) {
        Position positionInTiles = DrawEngine.getPixelsTranslatedToTiles(positionInPixels);
        int tileId = getTileIdOn(positionInTiles);
        return tileId == tilesSetup.TRANSPORTER_GATE_ID && mainFigure.getXPosModuloScale() == 0 && mainFigure.getYPosModuloScale() == 0;
    }

    private static Position getNextTransporterPositionInTiles(Position currentTransporterPositionInTiles, SpriteDirection direction) {
        int row = currentTransporterPositionInTiles.getyPos();
        int col = currentTransporterPositionInTiles.getxPos();
        switch (direction) {
            case LEFT -> {
                for (int lus = col - 1; lus > 0; lus--) {
                    col--;
                    if (tilesSetup.tileMapId[lus][row] == tilesSetup.TRANSPORTER_GATE_ID) {
                        col++;
                        break;
                    }
                }
                if (col <= 0) {
                    col = TilesSetup.TILE_MAP_COLS - 2;
                }
            }
            case RIGHT -> {
                for (int lus = col + 1; lus < TilesSetup.TILE_MAP_COLS - 1; lus++) {
                    col++;
                    if (tilesSetup.tileMapId[lus][row] == tilesSetup.TRANSPORTER_GATE_ID) {
                        col--;
                        break;
                    }
                }
                if (col >= TilesSetup.TILE_MAP_COLS - 1) {
                    col = 1;
                }
            }
            case UP -> {
                for (int lus = row - 1; lus > 0; lus--) {
                    row--;
                    if (tilesSetup.tileMapId[col][lus] == tilesSetup.TRANSPORTER_GATE_ID) {
                        row++;
                        break;
                    }
                }
                if (row <= 0) {
                    row = TilesSetup.TILE_MAP_ROWS - 2;
                }
            }
            case DOWN -> {
                for (int lus = row + 1; lus < TilesSetup.TILE_MAP_ROWS - 1; lus++) {
                    row++;
                    if (tilesSetup.tileMapId[col][lus] == tilesSetup.TRANSPORTER_GATE_ID) {
                        row--;
                        break;
                    }
                }
                if (row >= TilesSetup.TILE_MAP_ROWS - 1) {
                    row = 1;
                }
            }
        }
        return new Position(col, row);
    }

    private static boolean canWalkThatDirection(Position positionInPixels, SpriteDirection direction) {
        Position positionInTiles = DrawEngine.getPixelsTranslatedToTiles(positionInPixels);
        int tileId = getTileIdNextTo(positionInTiles, direction);
        return canWalkOnIdList.contains(tileId);
    }

    public static int getTileIdOn(Position positionInTiles) {
        int col = positionInTiles.getxPos();
        int row = positionInTiles.getyPos();
        if (col > TilesSetup.TILE_MAP_COLS - 1) {
            col = TilesSetup.TILE_MAP_COLS - 1;
        } else {
            if (col < 0) {
                col = 0;
            }
        }
        if (row > TilesSetup.TILE_MAP_ROWS - 1) {
            row = TilesSetup.TILE_MAP_ROWS - 1;
        } else {
            if (row < 0) {
                row = 0;
            }
        }
        return tilesSetup.tileMapId[col][row];
    }

    public static int getTileIdNextTo(Position positionInTiles, SpriteDirection direction) {
        int col = positionInTiles.getxPos();
        int row = positionInTiles.getyPos();
        switch (direction) {
            case LEFT -> col--;
            case RIGHT -> col++;
            case UP -> row--;
            case DOWN -> row++;
        }

        if (col < 0) {
            col = 0;
        } else {
            if (col > TilesSetup.TILE_MAP_COLS - 1) {
                col = TilesSetup.TILE_MAP_COLS - 1;
            }
        }
        if (row < 0) {
            row = 0;
        } else {
            if (row > TilesSetup.TILE_MAP_ROWS - 1) {
                row = TilesSetup.TILE_MAP_ROWS - 1;
            }
        }
        // System.out.println("Look to col " + col + " and row " + row);
        return tilesSetup.tileMapId[col][row];
    }

    private static void performZombiesWalking() {
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            zombieFigure.lineUpToTiles();
            Position positionInPixels = zombieFigure.getPosition();
            if (!zombieFigure.isInFreeze()) {
                if ((positionInPixels.getyPos() % DrawEngine.SCALE_FACTOR_SPRITE != 0) ||
                        (positionInPixels.getxPos() % DrawEngine.SCALE_FACTOR_SPRITE != 0) ||
                        canWalkThatDirection(positionInPixels, zombieFigure.getSpriteDirection())) {
                    if (Math.random() > 0.001) {
                        zombieFigure.walk();
                    } else {
                        zombieFigure.setNextDirection(positionInPixels);
                    }
                } else {
                    zombieFigure.setNextDirection(positionInPixels);
                }
            }
        }
    }



    public static boolean getIsKeyForMainSpritePressedAndHandleDirection() {
        boolean keyMovementPressed = false;
        if (getStateOfGame().equals(StateOfGame.play)) {
            if (KeyboardSetup.keyBuffer.contains("LEFT")) {
                if (mainFigure.getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) { // must complete Y
                    mainFigure.setDirection(SpriteDirection.LEFT);
                }
                keyMovementPressed = true;
            }
            if (KeyboardSetup.keyBuffer.contains("RIGHT")) {
                if (mainFigure.getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) {// must complete Y
                    mainFigure.setDirection(SpriteDirection.RIGHT);
                }
                keyMovementPressed = true;
            }
            if (KeyboardSetup.keyBuffer.contains("UP")) {
                if (mainFigure.getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) {// must complete X
                    mainFigure.setDirection(SpriteDirection.UP);
                }
                keyMovementPressed = true;
            }
            if (KeyboardSetup.keyBuffer.contains("DOWN")) {
                if (mainFigure.getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) {// must complete X
                    mainFigure.setDirection(SpriteDirection.DOWN);
                }
                keyMovementPressed = true;
            }
        }
        return keyMovementPressed;

    }



}
