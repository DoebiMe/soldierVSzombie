package logic;

import combinatedFields.Position;
import combinatedFields.RangePair;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;
import figures.*;
import scoreEngine.ScoreEngine;
import setups.IQ;
import setups.KeyboardSetup;
import setups.TilesSetup;
import soundEngine.SoundEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneralGameLogic {

    public static final List<Integer> canWalkOnIdList = List.of(0, 175, 181, 186, 187);

    private static StateOfGame stateOfGame;
    private static MainFigure mainFigure;
    private static TilesSetup tilesSetup;
    private static ZombieCollection zombieCollection;
    private static BulletCollection bulletCollection;
    private static SkullCollection skullCollection;
    private static GiftCollection giftCollection;


    public GeneralGameLogic() {
        generalGameLogicSetup();
    }

    public static void generalGameLogicSetup() {
        tilesSetup = new TilesSetup();
        mainFigure = new MainFigure();
        stateOfGame = StateOfGame.play;
        zombieCollection = new ZombieCollection(tilesSetup);
        bulletCollection = new BulletCollection(tilesSetup);
        skullCollection = new SkullCollection(tilesSetup);
        giftCollection = new GiftCollection(tilesSetup);

        //  https://edencoding.com/playing-audio/

    }

    public static StateOfGame getStateOfGame() {
        return stateOfGame;
    }
    public static void setStateOfGame(StateOfGame theStateOfGame) {
        stateOfGame = theStateOfGame;
    }
    private static SpriteDirection oldMainDirection = SpriteDirection.LEFT;
    private static int holdWalk = 10;

    public static void executeLogicTimeLine() {
        ProcessKeys.processPressedKeysGeneral();
        boolean keyMovementPressed = getIsKeyForMainSpritePressedAndHandleDirection();

        if (stateOfGame == StateOfGame.play) {
            mainFigure.setNextStep();

            if (oldMainDirection.equals(mainFigure.getDirection()) ) {
                if (holdWalk-- <= 0) {
                    mainFigure.walk(keyMovementPressed, canWalkThatDirection(mainFigure.getPosition(), mainFigure.getDirection()));
                    holdWalk = 0;
                }
            } else {
                holdWalk = 2; //for 2 scans no walking(so the sprite does'nt walk if user only wants to change direction)
            }
            oldMainDirection  = mainFigure.getDirection();

            boolean transporterMovement = isStandingOnTransporter(mainFigure.getPosition());
            if (transporterMovement) {
                Position newPositionInTiles = getNextTransporterPositionInTiles(
                        mainFigure.getPositionInTiles(), mainFigure.getDirection());
                Position newPositionInPixels = DrawEngine.getTilesTranslatedToPixels(newPositionInTiles);
                mainFigure.setPosition(newPositionInPixels);
                mainFigure.alignToTiles();
            }
            performZombiesWalking();
            performZombiesWalkingOnTransporter();
            performZombieFreezing();
            bulletCollection.handleAllBullets();
            eraseBulletsAgainstNonWalkable();
            skullCollection.handleAllSkulls(mainFigure.getPosition());
            ScoreEngine.addToKills(handleBulletsOnZombies());
            performCollisionCheck();
            DrawEngine.drawAllTiles(tilesSetup, mainFigure, transporterMovement);
            DrawEngine.drawAllGifts(mainFigure, giftCollection);
            DrawEngine.drawAllZombies(mainFigure, zombieCollection);
            DrawEngine.drawAllBullets(mainFigure, bulletCollection);
            DrawEngine.drawAllSkulls(mainFigure, skullCollection);
            DrawEngine.drawMainFigure(mainFigure);
            DrawEngine.drawScoreBoard();
        }
    }

    private static int hit = 0;

    public static void performCollisionCheck() {
        for (SkullFigure skullFigure : skullCollection.skullFigureList) {
            if (skullFigure.isInCoalition(mainFigure)) {
                System.out.println("Hit by skull " + hit++);
            }
        }
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            if (zombieFigure.isInCoalition(mainFigure)) {
                System.out.println("Hit by zombie " + hit++);
            }
        }

    }

    public static int handleBulletsOnZombies() {
        List<BulletFigure> bulletFigureListToErase = new ArrayList<>();
        List<ZombieFigure> zombieFigureListToErase = new ArrayList<>();
        int hits = 0;
        for (BulletFigure bulletFigure : bulletCollection.bulletFigureList) {
            for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
                if (zombieFigure.isPositionInsideZombieFigure(bulletFigure.getCenterPositionInPixels())) {
                    bulletFigureListToErase.add(bulletFigure);
                    zombieFigureListToErase.add(zombieFigure);
                    hits++;
                    break;
                }
            }
        }
        bulletCollection.bulletFigureList.removeAll(bulletFigureListToErase);
        zombieCollection.zombieFigureList.removeAll(zombieFigureListToErase);
        return hits;
    }

    public static void eraseBulletsAgainstNonWalkable() {
        List<BulletFigure> bulletFigureListToErase = new ArrayList<>();
        for (BulletFigure bulletFigure : bulletCollection.bulletFigureList) {

            if (!canWalkOnIdList.contains(getTileIdOn(bulletFigure.getPositionInTiles()))) {
                bulletFigureListToErase.add(bulletFigure);
            }
        }
        bulletCollection.bulletFigureList.removeAll(bulletFigureListToErase);
    }


    private static void performZombieFreezing() {
        Random random = new Random();
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            if (zombieFigure.getIq() == IQ.stupid) {
                zombieFigure.decrementFreezeCounter();
                if ((zombieFigure.getPosition().getxPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) &&
                        (zombieFigure.getPosition().getyPos() % DrawEngine.SCALE_FACTOR_SPRITE == 0) &&
                        (!zombieFigure.isInFreeze())) {
                    if (random.nextInt(2000) == 1000) {
                        zombieFigure.setFreezeCounter(200);
                    }
                }
            }
        }
    }

    private static void performZombiesWalkingOnTransporter() {
        for (ZombieFigure zombieFigure : zombieCollection.zombieFigureList) {
            if (isStandingOnTransporter(zombieFigure.getPosition())) {
                Position newPositionInTiles = getNextTransporterPositionInTiles(
                        zombieFigure.getPositionInTiles(), zombieFigure.getDirection());
                Position newPositionInPixels = DrawEngine.getTilesTranslatedToPixels(newPositionInTiles);
                zombieFigure.setPosition(newPositionInPixels);
                zombieFigure.alignToTiles();
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

    protected static boolean canWalkThatDirection(Position positionInPixels, SpriteDirection direction) {
        Position positionInTiles = DrawEngine.getPixelsTranslatedToTiles(positionInPixels);
        // System.out.println("Test position");
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
            //zombieFigure.lineUpToTiles();
            //setZombieOnTileAlignedWithDirection(zombieFigure);
            switch (zombieFigure.getIq()) {
                case stupid -> {
                }
                case normal -> {

                }
                case smart -> {
                    handleSmartZombie(zombieFigure);
                }
                // todo implement rest of IQ : einstein
            }

        }
    }

    private static void handleSmartZombie(ZombieFigure zombieFigure) {
        setZombieOnTileAlignedWithDirection(zombieFigure);
        Position startPosition = zombieFigure.getPosition();

        if (!zombieFigure.mayChangeDirection()) {
            zombieFigure.walk();
            return;
        }
        // so we may change direction ;-)
        if (isPathToMainFigureFree(zombieFigure)) {
            // do something
        }

        if (canWalkThatDirection(startPosition, zombieFigure.getDirection())) {
            zombieFigure.walk();
        } else {
            zombieFigure.setNextDirection();
        }

    }

    static long obstacleCounter = 0;
    static long cornerCounter = 0;

    private static boolean isPathToMainFigureFree(ZombieFigure zombieFigure) {
        Position zombiePosInTiles = zombieFigure.getPositionInTiles();
        Position mainFigurePosInTiles = mainFigure.getPositionInTiles();
        int maxX = Math.max(zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos());
        int minY = Math.min(zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos());
        int maxY = Math.max(zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos());
        int minX = Math.min(zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos());

        // simple, zombie is in straight line (row of col) form mainfigure
        boolean noObstakel = findEasyPath(zombieFigure, zombiePosInTiles, mainFigurePosInTiles, minX, maxX, minY, maxY);
        if (noObstakel) return noObstakel;

        // medium, zombie detects mainfigure behind 1 corner (row + col) or (col + row) , yes, there is a difference ;-)
        if (Math.random() <= 0.2) { // make sure sometimes first col, sometimes first row, with favor for col
            if (findMediumPathBehindTheCornerPrefCol(zombieFigure, isPartOfColFree(zombiePosInTiles.getxPos(), minY, maxY), isPartOfRowFree(mainFigurePosInTiles.getyPos(), minX, maxX), "see you behind the corner 1a : ", zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos(), SpriteDirection.DOWN, SpriteDirection.UP))
                return true;

            if (findMediumPathBehindTheCornerPrefRow(zombieFigure, isPartOfRowFree(zombiePosInTiles.getyPos(), minX, maxX), isPartOfColFree(mainFigurePosInTiles.getxPos(), minY, maxY), "see you behind the corner 2a : ", zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos(), SpriteDirection.RIGHT, SpriteDirection.LEFT))
                return true;
        } else {
            if (findMediumPathBehindTheCornerPrefRow(zombieFigure, isPartOfRowFree(zombiePosInTiles.getyPos(), minX, maxX), isPartOfColFree(mainFigurePosInTiles.getxPos(), minY, maxY), "see you behind the corner 2b : ", zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos(), SpriteDirection.RIGHT, SpriteDirection.LEFT))
                return true;
            if (findMediumPathBehindTheCornerPrefCol(zombieFigure, isPartOfColFree(zombiePosInTiles.getxPos(), minY, maxY), isPartOfRowFree(mainFigurePosInTiles.getyPos(), minX, maxX), "see you behind the corner a : ", zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos(), SpriteDirection.DOWN, SpriteDirection.UP))
                return true;
        }

        // hard
        // 1) get rowrange on zombiePosition
        // 2) for each (step 1) get colRange on forloop,zombiePositionCol (include start AND end in the for loop!)
        // 3) for each (step 2) get colRange on forloop,forloop

        if (findHardPathPrefCol(zombieFigure, zombiePosInTiles, mainFigurePosInTiles)) return true;
        if (findHardPathPrefRow(zombieFigure, zombiePosInTiles, mainFigurePosInTiles)) return true;

        return noObstakel;
    }

    private static boolean findHardPathPrefCol(ZombieFigure zombieFigure, Position zombiePosInTiles, Position mainFigurePosInTiles) {
        RangePair rangeColsStep1 = getWalkableColRangePairFromPosition(zombiePosInTiles);
        for (int loopColsFromStep1 = rangeColsStep1.getMin(); loopColsFromStep1 <= rangeColsStep1.getMax(); loopColsFromStep1++) {
            RangePair rangeRowsStep2 = getWalkableRowRangePairFromPosition(new Position(loopColsFromStep1, zombiePosInTiles.getyPos()));
            for (int loopRowsFromStep2 = rangeRowsStep2.getMin(); loopRowsFromStep2 <= rangeRowsStep2.getMax(); loopRowsFromStep2++) {
                RangePair rangeColsStep3 = getWalkableColRangePairFromPosition(new Position(loopColsFromStep1, loopRowsFromStep2));
                Position testPositionStep2 = new Position(loopColsFromStep1, loopRowsFromStep2);
                if (testPositionStep2.equals(mainFigurePosInTiles)) {
                    System.out.println("During step 2 pref Col Nailed it on " + testPositionStep2.getxPos() + ":" + testPositionStep2.getyPos());
                    zombieFigure.setDirection(zombiePosInTiles.getxPos() < loopColsFromStep1
                            ? SpriteDirection.RIGHT
                            : SpriteDirection.LEFT);
                    return true;
                }
                for (int loopColsFromStep3 = rangeColsStep3.getMin(); loopColsFromStep3 <= rangeColsStep3.getMax(); loopColsFromStep3++) {
                    Position testPositionStep3 = new Position(loopColsFromStep3, loopRowsFromStep2);
                    if (testPositionStep3.equals(mainFigurePosInTiles)) {
                        System.out.println("During step 3 pref Col Nailed it on " + testPositionStep3.getxPos() + ":" + testPositionStep3.getyPos());
                        zombieFigure.setDirection(zombiePosInTiles.getxPos() < loopColsFromStep1
                                ? SpriteDirection.RIGHT
                                : SpriteDirection.LEFT);
                        return true;
                    }
/*
                    // step 4
                    RangePair rangeRowsStep4 = getWalkableRowRangePairFromPosition(testPositionStep3);
                    for (int loopRowsFromStep4 = rangeRowsStep4.getMin();loopRowsFromStep4 <= rangeRowsStep4.getMax();loopRowsFromStep4++) {
                        Position testPositionStep4 = new Position(loopColsFromStep3,loopRowsFromStep4);
                        if (testPositionStep4.equals(mainFigurePosInTiles)) {
                            System.out.println("During step 4 pref Col Nailed it on " + testPositionStep4.getxPos()+":"+testPositionStep4.getyPos());
                            // here we could stop
                            zombieFigure.setDirection(zombiePosInTiles.getxPos() < loopColsFromStep1
                                    ? SpriteDirection.RIGHT
                                    : SpriteDirection.LEFT);
                            return true;
                        }
                    }
*/
                }

            }
        }
        return false;
    }

    private static boolean findHardPathPrefRow(ZombieFigure zombieFigure, Position zombiePosInTiles, Position mainFigurePosInTiles) {
        //step 1
        RangePair rangeRowsStep1 = getWalkableRowRangePairFromPosition(zombiePosInTiles);
        //System.out.println("Step 1 Zombie row range start : " + rangeFreeRowsZombie.getMin()+" and end : "+ rangeFreeRowsZombie.getMax());
        //step 2
        for (int loopRowsFromStep1 = rangeRowsStep1.getMin(); loopRowsFromStep1 <= rangeRowsStep1.getMax(); loopRowsFromStep1++) {
            RangePair rangeColsStep2 = getWalkableColRangePairFromPosition(new Position(zombiePosInTiles.getxPos(), loopRowsFromStep1));
            for (int loopColsFromStep2 = rangeColsStep2.getMin(); loopColsFromStep2 <= rangeColsStep2.getMax(); loopColsFromStep2++) {
                Position testPositionStep2 = new Position(loopColsFromStep2, loopRowsFromStep1);
                if (testPositionStep2.equals(mainFigurePosInTiles)) {
                    System.out.println("During step 2 pref Row Nailed it on " + testPositionStep2.getxPos() + ":" + testPositionStep2.getyPos());
                    // here we could stop
                    zombieFigure.setDirection(zombiePosInTiles.getyPos() < loopRowsFromStep1
                            ? SpriteDirection.DOWN
                            : SpriteDirection.UP);
                    return true;
                }

                RangePair rangeRowsStep3 = getWalkableRowRangePairFromPosition(new Position(loopColsFromStep2, loopRowsFromStep1));
                //System.out.println("Step 3 For row "+rowsWhereZombieCanWalkOn+" range col :"+rowsWhereZombieCanWalkOn+" is the rowRange start : "+ rangeFreeRows.getMin()+" and end : "+ rangeFreeRows.getMax());
                for (int loopRowsFromStep3 = rangeRowsStep3.getMin(); loopRowsFromStep3 <= rangeRowsStep3.getMax(); loopRowsFromStep3++) {
                    Position testPositionStep3 = new Position(loopColsFromStep2, loopRowsFromStep3);
                    if (testPositionStep3.equals(mainFigurePosInTiles)) {
                        System.out.println("During step 3 pref Row Nailed it on " + testPositionStep3.getxPos() + ":" + testPositionStep3.getyPos());
                        // here we could stop
                        zombieFigure.setDirection(zombiePosInTiles.getyPos() < loopRowsFromStep1
                                ? SpriteDirection.DOWN
                                : SpriteDirection.UP);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static RangePair getWalkableColRangePairFromPosition(Position positionInTiles) {
        int posColMin = positionInTiles.getxPos();
        int posColMax = positionInTiles.getxPos();
        while (posColMin > 0 && canWalkOnIdList.contains(getTileIdOn(new Position(posColMin - 1, positionInTiles.getyPos())))) {
            posColMin--;
        }
        while (posColMax < TilesSetup.TILE_MAP_COLS - 1 && canWalkOnIdList.contains(getTileIdOn(new Position(posColMax + 1, positionInTiles.getyPos())))) {
            posColMax++;
        }
        return new RangePair(posColMin, posColMax);
    }

    private static RangePair getWalkableRowRangePairFromPosition(Position positionInTiles) {
        int posRowMin = positionInTiles.getyPos();
        int posRowMax = positionInTiles.getyPos();
        while (posRowMin > 0 && canWalkOnIdList.contains(getTileIdOn(new Position(positionInTiles.getxPos(), posRowMin - 1)))) {
            posRowMin--;
        }
        while (posRowMax < TilesSetup.TILE_MAP_ROWS - 1 && canWalkOnIdList.contains(getTileIdOn(new Position(positionInTiles.getxPos(), posRowMax + 1)))) {
            posRowMax++;
        }
        return new RangePair(posRowMin, posRowMax);
    }


    private static boolean findMediumPathBehindTheCornerPrefRow(ZombieFigure zombieFigure, boolean partOfRowFree, boolean partOfColFree, String s, int i, int i2, SpriteDirection right, SpriteDirection left) {
        if (partOfRowFree) {
            if (partOfColFree) {
                //System.out.println(s + cornerCounter++);
                zombieFigure.setDirection(i < i2 //
                        ? right
                        : left);
                return true;
            }
        }
        return false;
    }

    private static boolean findMediumPathBehindTheCornerPrefCol(ZombieFigure zombieFigure, boolean partOfColFree, boolean partOfRowFree, String s, int i, int i2, SpriteDirection down, SpriteDirection up) {
        if (partOfColFree) {
            if (partOfRowFree) {
                //System.out.println(s + cornerCounter++);
                zombieFigure.setDirection(i < i2 //
                        ? down
                        : up);
                return true;
            }
        }
        return false;
    }

    private static boolean isPartOfRowFree(int row, int colMin, int colMax) {
        for (int lusX = colMin; lusX < colMax; lusX++) {
            if (!canWalkOnIdList.contains(getTileIdOn(new Position(lusX, row)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPartOfColFree(int col, int rowMin, int rowMax) {

        for (int lusY = rowMin; lusY < rowMax; lusY++) {
            if (!canWalkOnIdList.contains(getTileIdOn(new Position(col, lusY)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean findEasyPath(ZombieFigure zombieFigure, Position zombiePosInTiles, Position mainFigurePosInTiles, int minX, int maxX, int minY, int maxY) {
        boolean noObstakel = true;
        if (minY == maxY) {
            //System.out.println("Main X = "+ mainFigurePosInTiles.getxPos()+" Main Y = "+ mainFigurePosInTiles.getyPos()+" zombie x = " + zombiePosInTiles.getxPos()+" zombie y = " + zombiePosInTiles.getyPos());
            for (int lusX = minX; lusX < maxX; lusX++) {
                if (!canWalkOnIdList.contains(getTileIdOn(new Position(lusX, minY)))) {
                    noObstakel = false;
                    break;
                }
            }
            if (noObstakel) {
                obstacleCounter++;
                // System.out.println("No obstakel horizontal : " + obstacleCounter);
                zombieFigure.setDirection(zombiePosInTiles.getxPos() < mainFigurePosInTiles.getxPos()
                        ? SpriteDirection.RIGHT
                        : SpriteDirection.LEFT);
                return true;
            }

        }
        if (minX == maxX) {
            for (int lusY = minY; lusY < maxY; lusY++) {
                if (!canWalkOnIdList.contains(getTileIdOn(new Position(minX, lusY)))) {
                    noObstakel = false;
                    break;
                }
            }
            if (noObstakel) {
                obstacleCounter++;
                // System.out.println("No obstakel vertical : " + obstacleCounter);
                zombieFigure.setDirection(zombiePosInTiles.getyPos() < mainFigurePosInTiles.getyPos()
                        ? SpriteDirection.DOWN
                        : SpriteDirection.UP);
                return true;
            }
        }
        return false;
    }


    private static void setZombieOnTileAlignedWithDirection(ZombieFigure zombieFigure) {
        int xpos = zombieFigure.getPosition().getxPos();
        int ypos = zombieFigure.getPosition().getyPos();

        switch (zombieFigure.getDirection()) {
            case UP, DOWN -> {
                int mod = xpos % DrawEngine.SCALE_FACTOR_SPRITE;
                if (mod != 0) {
                    xpos -= mod;

                }
            }
            case LEFT, RIGHT -> {
                int mod = ypos % DrawEngine.SCALE_FACTOR_SPRITE;
                if (mod != 0) {
                    ypos -= mod;

                }
            }
        }
        zombieFigure.setPosition(new Position(xpos, ypos));

    }

    private static boolean previousScanHadSpace = false;
    private static int counterSpace = 10;

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

            counterSpace--;
            if (counterSpace <= 0) {
                if (ScoreEngine.getRemainingBullets() >0) {
                    counterSpace = 5;
                    if (KeyboardSetup.keyBuffer.contains("SPACE") && !previousScanHadSpace) {
                        previousScanHadSpace = true;
                        bulletCollection.addNewBullet(mainFigure);
                        SoundEngine.startShoot();
                        ScoreEngine.setRemainingBullets(ScoreEngine.getRemainingBullets()-1);
                    } else {
                        previousScanHadSpace = false;
                    }
                }
            }



        }
        return keyMovementPressed;

    }

}
