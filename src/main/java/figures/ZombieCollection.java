package figures;

import combinatedFields.Position;
import combinatedFields.SpriteDirection;
import drawEngine.DrawEngine;
import logic.GeneralGameLogic;
import setups.IQ;
import setups.TilesSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieCollection {
    public  List<ZombieFigure> zombieFigureList;
    private TilesSetup tilesSetup;

    public ZombieCollection(TilesSetup tilesSetup) {
        this.tilesSetup = tilesSetup;
        zombieCollectionSetup();
    }

    public  void zombieCollectionSetup() {
        zombieFigureList = new ArrayList<>();

        for (int count = 0; count < 50;count++) {
            ZombieFigure zombieFigure = new ZombieFigure();
            zombieFigure.setPosition(getRandomFreePosition());
            zombieFigure.setDirection(SpriteDirection.LEFT);
            zombieFigure.setNextDirection();
            zombieFigure.setIq(IQ.smart);
            zombieFigureList.add(zombieFigure);
        }

    }

    private Position getRandomFreePosition() {
        Random random = new Random();
        int col;
        int row;
        do {
            col = random.nextInt(TilesSetup.TILE_MAP_COLS-2)+1;
            row = random.nextInt(TilesSetup.TILE_MAP_ROWS-2)+1;

        } while (!GeneralGameLogic.canWalkOnIdList.contains(tilesSetup.tileMapId[col][row]));
        return DrawEngine.getTilesTranslatedToPixels(new Position(col,row));
    }


}
