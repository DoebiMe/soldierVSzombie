package logic;

import combinatedFields.Position;
import combinatedFields.RangePair;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;
import figures.MainFigure;
import figures.ZombieCollection;
import figures.ZombieFigure;
import setups.IQ;
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
                        mainFigure.getPositionInTiles(), mainFigure.getDirection());
                Position newPositionInPixels = DrawEngine.getTilesTranslatedToPixels(newPositionInTiles);
                mainFigure.setPosition(newPositionInPixels);
                mainFigure.allignToTiles();
            }

            performZombiesWalking();
            performZombiesWalkingOnTransporter();
            performZombieFreezing();
            DrawEngine.drawAllTiles(tilesSetup, mainFigure, transporterMovement);
            DrawEngine.drawAllZombies(mainFigure, zombieCollection);
            DrawEngine.drawMainFigure(mainFigure);
            /*
            RangePair rangePairCol = getWalkableColRangePairFromPosition(mainFigure.getPositionInTiles());
            System.out.println("Main col range start : " + rangePairCol.getMin()+" and end : "+ rangePairCol.getMax());
            RangePair rangePairRow = getWalkableRowRangePairFromPosition(mainFigure.getPositionInTiles());
            System.out.println("Main row range start : " + rangePairRow.getMin()+" and end : "+ rangePairRow.getMax());
             */
        }
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
        if (Math.random() < 0.1) { // make sure sometimes first col, sometimes first row, with favor for col
            if (findMediumPathBehindTheCornerPrefCol(zombieFigure, isPartOfColFree(zombiePosInTiles.getxPos(), minY, maxY), isPartOfRowFree(mainFigurePosInTiles.getyPos(), minX, maxX), "see you behind the corner 1 : ", zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos(), SpriteDirection.DOWN, SpriteDirection.UP))
                return true;

            if (findMediumPathBehindTheCornerPrefRow(zombieFigure, isPartOfRowFree(zombiePosInTiles.getyPos(), minX, maxX), isPartOfColFree(mainFigurePosInTiles.getxPos(), minY, maxY), "see you behind the corner 2 : ", zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos(), SpriteDirection.RIGHT, SpriteDirection.LEFT))
                return true;
        } else {
            if (findMediumPathBehindTheCornerPrefRow(zombieFigure, isPartOfRowFree(zombiePosInTiles.getyPos(), minX, maxX), isPartOfColFree(mainFigurePosInTiles.getxPos(), minY, maxY), "see you behind the corner 2 : ", zombiePosInTiles.getxPos(), mainFigurePosInTiles.getxPos(), SpriteDirection.RIGHT, SpriteDirection.LEFT))
                return true;
            if (findMediumPathBehindTheCornerPrefCol(zombieFigure, isPartOfColFree(zombiePosInTiles.getxPos(), minY, maxY), isPartOfRowFree(mainFigurePosInTiles.getyPos(), minX, maxX), "see you behind the corner 1 : ", zombiePosInTiles.getyPos(), mainFigurePosInTiles.getyPos(), SpriteDirection.DOWN, SpriteDirection.UP))
                return true;
        }

        // hard
        // 1) get rowrange on zombiePosition
        // 2) for each (step 1) get colRange on forloop,zombiePositionCol (include start AND end in the for loop!)
        // 3) for each (step 2) get colRange on forloop,forloop

        //step 1
        RangePair rangeFreeColsZombie =  getWalkableColRangePairFromPosition(zombiePosInTiles);
        System.out.println("Step 1 Zombie col range start : " + rangeFreeColsZombie.getMin()+" and end : "+ rangeFreeColsZombie.getMax());
        //step 2
        for (int colsWhereZombieCanWalkOn = rangeFreeColsZombie.getMin();colsWhereZombieCanWalkOn<=rangeFreeColsZombie.getMax();colsWhereZombieCanWalkOn++) {
            RangePair rangeFreeRows = getWalkableRowRangePairFromPosition(new Position(colsWhereZombieCanWalkOn,zombiePosInTiles.getyPos()));
            System.out.println("Step 2 For col "+colsWhereZombieCanWalkOn+" range row start : "+ rangeFreeRows.getMin()+" and end : " + rangeFreeRows.getMax());
            //step 3
            for (int rows = rangeFreeRows.getMin();rows<=rangeFreeRows.getMax();rows++) {
                RangePair rangeFreeCols = getWalkableColRangePairFromPosition(new Position(colsWhereZombieCanWalkOn,rows));
                System.out.println("Step 3 For col "+colsWhereZombieCanWalkOn+" range row :"+colsWhereZombieCanWalkOn+" is the colRange start : "+ rangeFreeCols.getMin()+" and end : "+ rangeFreeCols.getMax());
                for (int cols = rangeFreeCols.getMin();cols<=rangeFreeCols.getMax();cols++) {
                    Position position = new Position(cols,rows);
                    if (position.equals(mainFigurePosInTiles)) {
                        System.out.println("Nailed it on " + position.getxPos()+":"+position.getyPos());
                        // here we could stop
                    }
                }
            }
        }



        return noObstakel;
    }

    private static RangePair getWalkableColRangePairFromPosition(Position positionInTiles) {
        int posColMin = positionInTiles.getxPos();
        int posColMax = positionInTiles.getxPos();
        while (posColMin > 0 && canWalkOnIdList.contains(getTileIdOn(new Position(posColMin-1,positionInTiles.getyPos())))) {
            posColMin--;
        }
        while (posColMax < TilesSetup.TILE_MAP_COLS-1 && canWalkOnIdList.contains(getTileIdOn(new Position(posColMax+1,positionInTiles.getyPos())))) {
            posColMax++;
        }
        return new RangePair(posColMin,posColMax);
    }
    private static RangePair getWalkableRowRangePairFromPosition(Position positionInTiles) {
        int posRowMin = positionInTiles.getyPos();
        int posRowMax = positionInTiles.getyPos();
        while (posRowMin > 0 && canWalkOnIdList.contains(getTileIdOn(new Position(positionInTiles.getxPos(), posRowMin-1)))) {
            posRowMin--;
        }
        while (posRowMax < TilesSetup.TILE_MAP_ROWS-1 && canWalkOnIdList.contains(getTileIdOn(new Position(positionInTiles.getxPos(),posRowMax+1)))) {
            posRowMax++;
        }
        return new RangePair(posRowMin,posRowMax);
    }


    private static boolean findMediumPathBehindTheCornerPrefRow(ZombieFigure zombieFigure, boolean partOfRowFree, boolean partOfColFree, String s, int i, int i2, SpriteDirection right, SpriteDirection left) {
        if (partOfRowFree) {
            if (partOfColFree) {
                System.out.println(s + cornerCounter++);
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
                System.out.println(s + cornerCounter++);
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
                    noObstakel=false;
                    break;
                }
            }
            if (noObstakel) {
                obstacleCounter++;
                System.out.println("No obstakel horizontal : " + obstacleCounter);
                zombieFigure.setDirection(zombiePosInTiles.getxPos() < mainFigurePosInTiles.getxPos()
                        ? SpriteDirection.RIGHT
                        : SpriteDirection.LEFT);
                return true;
            }

        }
        if (minX == maxX) {
            for (int lusY = minY; lusY < maxY; lusY++) {
                if (!canWalkOnIdList.contains(getTileIdOn(new Position(minX, lusY)))) {
                    noObstakel=false;
                    break;
                }
            }
            if (noObstakel) {
                obstacleCounter++;
                System.out.println("No obstakel vertical : " + obstacleCounter);
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



/*
    private static boolean getPerformZombieWalkForSmartWorked(ZombieFigure zombieFigure) {
        boolean result;
        SpriteDirection spriteDirection;
        int zombieNewXPos = zombieFigure.getPositionInPixels().getxPos();
        int zombieNewYPos = zombieFigure.getPositionInPixels().getyPos();
        int zombieXPosInTiles = zombieFigure.getPositionInTiles().getxPos();
        int zombieYPosInTiles = zombieFigure.getPositionInTiles().getyPos();
        int mainFigureXPosInTiles = mainFigure.getPositionInTiles().getxPos();
        int mainFigureYPosInTiles = mainFigure.getPositionInTiles().getyPos();

        if (zombieFigure.isPositionAllignedWithTiles() &&
                (!isStandingOnTransporter(zombieFigure.getPositionInPixels()))) {
            System.out.println("Beiing smart");
            // step A : Horizontal approach, followed by vertical
            //      take all theTileId's from the zombiefigure colnumber until the mainfigure colnumber
            //      = TileIDs[zombieCol][zombieRow] until TileIDs[mainFigureCol][zombieRow],  mark, zombieRow is the constant
            //      canWalkOnThisPartOfRow(
            //            zombieFigure.getPostionInTiles.getY(), // = row zombieFigure
            //            zombieFigure.getPostionInTiles.getX(),
            //            mainFigure.getPostionInTiles.getX())
            //
            //      take all TheTileId's from the mainfigure rownumber until the zombiefigure rownumber
            //      = TileIDs[mainFigureCol][mainfigureRow] until TileIDs[mainFigureCol][zombieFigureRow], mark, mainFigureCol is the constant
            //      canWalkOnThisPartOfCol(
            //            mainFigure.getPostionInTiles.getX(), // = col mainFigure
            //            mainFigure.getPostionInTiles.getY(),
            //            zombieFigure.getPostionInTiles.getY())
            //      if all TileId's are walkable, go that direction (either left or right)

            result = canWalkOnThisPartOfRow( //
                    zombieYPosInTiles, zombieXPosInTiles, mainFigureXPosInTiles);
            System.out.println(LocalTime.now().getSecond() + " Step A Can walk on this part of row " + zombieYPosInTiles + " xpos1=" + zombieXPosInTiles + " xpos2=" + mainFigureXPosInTiles + " result :" + result);
            result &= canWalkOnThisPartOfCol(//
                    mainFigureXPosInTiles, mainFigureYPosInTiles, zombieYPosInTiles);
            System.out.println(LocalTime.now().getSecond() + " Step A Can walk on this part of col " + zombieXPosInTiles + " ypos1=" + zombieYPosInTiles + " xpos2=" + mainFigureYPosInTiles + " result :" + result);
            if (result) {
                System.out.println("Found Step A");

                // BUG : COULD BE YPOS WHO NEEDS TO CHANGE IF THE mainFigure is on same row as zombieFigure

                spriteDirection =
                        zombieXPosInTiles < mainFigureXPosInTiles //
                                ? SpriteDirection.RIGHT //
                                : SpriteDirection.LEFT;
                zombieFigure.setSpriteDirection(spriteDirection);
                return true;
            } else {
                System.out.println("NOT found step A");
            }

            // Step B : Vertical approach, followed by horizontal
            //      take all theTileId's from the zombiefigure rownumber until the mainfigure rownumber
            //      (= TileIDs[zombieCol][zombieRow] until TileIDs[zombieCol][mainFigureRow],  mark, zombiecol is the constant
            //      canWalkOnThisPartOfCol(
            //            zombieFigure.getPositionInTiles.getX(), // = col zombieFigure
            //            zombieFigure.getPositionInTiles.getY(),
            //            mainFigure.getPositionInTiles.getY();
            //
            //      take all TheTileId's from the zombiefigure colnumber until the mainfigure colnumber
            //      (= TileIDs[zombieFigureCol][mainFigureRow] until TileIDs[mainFigureCol][mainFigureRow], mark, mainFigureRow is the constant
            //      canWalkOnThisPartOfRow(
            //            mainFigure.getPositionInTiles.getY(), // = row mainFigure
            //            zombieFigure.getPositionInTiles.getX(),
            //            mainFigure.getPositionInTiles.getX();
            //      if all TileId's are walkable, go that direction (either up or down)

            //      if all TileId's are walkable, go that direction

            result = canWalkOnThisPartOfCol(zombieXPosInTiles, zombieYPosInTiles, mainFigureYPosInTiles) //
                    && //
                    canWalkOnThisPartOfRow(mainFigureYPosInTiles, zombieXPosInTiles, mainFigureXPosInTiles);
            if (result) {
                System.out.println("Found step B");

                // BUG : COULD BE XPOS WHO NEEDS TO CHANGE IF THE mainFigure is on same col as zombieFigure

                spriteDirection = zombieYPosInTiles < mainFigureYPosInTiles //
                        ? SpriteDirection.DOWN //
                        : SpriteDirection.UP;
                zombieFigure.setSpriteDirection(spriteDirection);
                return true;
            } else {
                System.out.println("NOT FOUND STEP B");
            }
        }
        return false;
    }

 */

    private static boolean canWalkOnThisPartOfRow(int row, int col1, int col2) {
        int colEnd = Math.max(col1, col2);
        int colStart = Math.min(col1, col2);
        //System.out.println("row = " + row + " colStart = " + colStart + " colEnd = " + colEnd);
        for (int lus = colStart; lus < colEnd; lus++) {
            if (!canWalkOnIdList.contains(tilesSetup.tileMapId[lus][row])) {
                return false;
            }
        }
        //System.out.println("ROW PART IS CLEAR");
        return true; // meaning the path is clear to walk on
    }

    private static boolean canWalkOnThisPartOfCol(int col, int row1, int row2) {
        int rowEnd = Math.max(row1, row2);
        int rowStart = Math.min(row1, row2);
        //System.out.println("col = " + col + " rowStart = " + rowStart + " rowEnd = " + rowEnd);
        for (int lus = rowStart; lus < rowEnd; lus++) {
            if (!canWalkOnIdList.contains(tilesSetup.tileMapId[col][lus])) {
                return false;
            }
        }
        //System.out.println("COL PART IS CLEAR");
        return true; // meaning the path is clear to walk on
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
